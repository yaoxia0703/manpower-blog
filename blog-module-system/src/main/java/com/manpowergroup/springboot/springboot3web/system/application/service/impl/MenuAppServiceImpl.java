package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.MenuType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import com.manpowergroup.springboot.springboot3web.system.application.assembler.MenuAssembler;
import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuStatusUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.vo.MenuTreeVo;
import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RoleMenu;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.MenuRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.menu.MenuMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.MenuAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role.RoleMenuMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * システムメニュー管理テーブル 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@Service
@Slf4j
@AllArgsConstructor
public class MenuAppServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuAppService {

    private final MenuRepository menuRepository;
    private final RoleMenuMapper roleMenuMapper;


    @Override
    public List<MenuTreeVo> selectAllMenus() {

        // 1. 全件取得（有効なものだけ、表示順でソート）
        List<Menu> menus = baseMapper.selectList(
                Wrappers.<Menu>lambdaQuery()
                        .eq(Menu::getStatus, Status.ENABLED)
                        .orderByAsc(Menu::getSort)
                        .orderByAsc(Menu::getId)
        );

        if (menus.isEmpty()) {
            return List.of();
        }

        //2. Entity -> Vo 変換
        List<MenuTreeVo> voList = menus.stream()
                .map(MenuAssembler::toTreeVo)
                .toList();

        // 3. parentId でグルーピング
        Map<Long, List<MenuTreeVo>> parentMap = voList.stream()
                .collect(Collectors.groupingBy(MenuTreeVo::getParentId));

        // 4. 再帰的にツリー構造を構築して返却
        return buildTree(parentMap, 0L);
    }

    @Override
    public List<MenuTreeVo> selectMenusByUserId(Long userId) {
        if (userId == null) {
            log.warn("[MenuAppService#selectMenusByUserId] userId is null");
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ユーザーIDが指定されていません");
        }
        return menuRepository.selectMenusByUserId(userId);
    }

    @Override
    @Transactional
    public Long createMenu(MenuSaveOrUpdateRequest request) {
        log.info("[MenuAppService#createMenu] start: request={}", request);

        // 親メニュー存在チェック（Service責務）
        if (request.parentId() != 0) {
            final var parent = baseMapper.selectById(request.parentId());
            if (parent == null) {
                log.warn("[MenuAppService#createMenu] parent not found parentId={}", request.parentId());
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "親メニューが存在しません");
            }
        }

        // Entity生成
        final var entity = MenuAssembler.toNewEntity(request);

        // 種別ルールチェック（Domain）
        entity.validateByType();

        // 同一階層の名称重複チェック（DB→Domain）
        final var nameExists = menuRepository.existsByParentIdAndName(
                request.parentId(),
                request.name()
        );
        entity.validateDuplicateName(nameExists > 0);

        // path重複チェック（DB→Domain）
        if (StringUtils.hasText(request.path())) {
            final var pathCount = menuRepository.countByPath(request.path());
            entity.validateDuplicatePath(pathCount > 0);
        }

        // 保存
        baseMapper.insert(entity);

        log.info("[MenuAppService#createMenu] success: id={}", entity.getId());

        return entity.getId();
    }

    @Override
    @Transactional
    public void updateMenu(Long id, MenuSaveOrUpdateRequest request) {
        log.info("[MenuAppService#updateMenu] start: id={}, request={}", id, request);

        // 既存チェック
        final var existing = baseMapper.selectById(id);
        if (existing == null) {
            log.warn("[MenuAppService#updateMenu] not found existing={}", id);
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "メニューが存在しません");
        }
        final MenuType oldType = existing.getType();

        // Entity更新
        MenuAssembler.applyToExisting(request, existing);

        // 自身を親に設定禁止（Domain）
        existing.validateNotSelfParent(id);

        // 親メニュー存在チェック（Service）
        if (request.parentId() != 0) {
            final var parent = baseMapper.selectById(request.parentId());
            if (parent == null) {
                log.warn("[MenuAppService#updateMenu] parent not found parentId={}", request.parentId());
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "親メニューが存在しません");
            }
        }

        // 循環チェック（Service）
        checkParentNotChild(id, request.parentId());

        // type変更制御（Domain）
        final var childrenCount = menuRepository.countByParentId(id);
        existing.validateTypeChange(oldType, request.type(), childrenCount > 0);

        // 種別ルールチェック（Domain）
        existing.validateByType();

        // 名称重複チェック（DB→Domain）
        final var nameCount = menuRepository.countByParentIdAndNameExcludeId(
                request.parentId(),
                request.name(),
                id
        );
        existing.validateDuplicateName(nameCount > 0);

        // path重複チェック（DB→Domain）
        if (StringUtils.hasText(request.path())) {
            final var pathCount = menuRepository.countByPathExcludeId(request.path(), id);
            existing.validateDuplicatePath(pathCount > 0);
        }

        // 更新
        baseMapper.updateById(existing);

        log.info("[MenuAppService#updateMenu] success: id={}", id);
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) {

        // 参数チェック
        if (id == null) {
            log.warn("[MenuAppService#deleteMenu] id is null");
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "メニューIDが指定されていません");
        }

        // 存在チェック
        final var existing = baseMapper.selectById(id);
        if (existing == null) {
            log.warn("[MenuAppService#deleteMenu] menu not found id={}", id);
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "メニューが存在しません");
        }

        // 子ノード存在チェック
        final var childCount = menuRepository.countByParentId(id);

        final long usedCount = roleMenuMapper.selectCount(
                Wrappers.<RoleMenu>lambdaQuery()
                        .eq(RoleMenu::getMenuId, id)
        );

        // 削除制御チェック（Domain）
        existing.validateDeletable(childCount > 0, usedCount > 0);

        // 関連するロール-メニューの紐付け削除
        roleMenuMapper.delete(
                Wrappers.<RoleMenu>lambdaQuery()
                        .eq(RoleMenu::getMenuId, id)
        );
        log.info("[MenuAppService#deleteMenu] related role-menu associations deleted: menuId={}", id);

        // メニュー削除
        baseMapper.deleteById(id);
        log.info("[MenuAppService#deleteMenu] menu deleted successfully: id={}", id);
    }

    @Override
    @Transactional
    public void changeMenuStatus(Long id, MenuStatusUpdateRequest request) {
        log.info("[MenuAppService#changeStatus] start: id={}, status={}", id, request.status());

        //IDの存在チェック
        if (id == null) {
            log.warn("[MenuAppService#changeStatus] id is null");
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "メニューIDが指定されていません");
        }
        // ステータスの存在チェック
        if (request.status() == null) {
            log.warn("[MenuAppService#changeStatus] status is null");
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ステータスが指定されていません");
        }
        // メニューの存在チェック
        final var menu = baseMapper.selectById(id);
        if (menu == null) {
            log.warn("[MenuAppService#changeStatus] menu not found id={}", id);
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "メニューが存在しません");
        }
        //以前のステータスを保持（ログ用）※Domainで変更後は取得できないため、ここで取得しておく
        final var oldStatus = menu.getStatus();

        // Domainで状態変更
        menu.changeStatus(request.status());

        baseMapper.updateById(menu);

        log.info(
                "[MenuAppService#changeStatus] status changed successfully: id={}, oldStatus={}, newStatus={}",
                id,
                oldStatus,
                request.status()
        );
    }





    /**
     * 親子関係の循環チェック（子ノードを親に設定することを防止）
     */
    private void checkParentNotChild(Long id, Long parentId) {
        log.info("[MenuAppService#checkParentNotChild] start: id={}, parentId={}", id, parentId);

        Long current = parentId;

        while (current != 0) {
            if (current.equals(id)) {
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "子ノードを親に設定できません");
            }

            final var menu = baseMapper.selectById(current);

            if (menu == null) {
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "メニュー構造が不正です");
            }

            current = menu.getParentId();
        }
    }

    /**
     * 再帰的にツリー構造を構築するヘルパーメソッド
     *
     * @param parentMap parentId をキー、子ノードのリストを値とするマップ
     * @param parentId  現在の親ID（最初は0からスタート）
     * @return 親IDに紐づく子ノードのリスト（子ノードも再帰的に構築済み）
     */
    private List<MenuTreeVo> buildTree(Map<Long, List<MenuTreeVo>> parentMap, Long parentId) {

        List<MenuTreeVo> children = parentMap.get(parentId);

        if (children == null) {
            return List.of();
        }

        for (MenuTreeVo node : children) {
            List<MenuTreeVo> subChildren = buildTree(parentMap, node.getId());
            node.setChildren(subChildren);
        }

        return children;
    }
}
