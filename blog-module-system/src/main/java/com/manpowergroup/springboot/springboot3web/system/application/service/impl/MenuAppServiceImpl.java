package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import com.manpowergroup.springboot.springboot3web.system.application.assembler.MenuAssembler;
import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuSaveOrUpdateRequest;
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
    public List<Menu> selectMenusByUserId(Long userId) {
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

        // 1. 親メニュー存在チェック（0はトップ）
        if (request.parentId() != 0) {
            final var parent = baseMapper.selectById(request.parentId());
            if (parent == null) {
                log.warn("[MenuAppService#createMenu] parent not found parentId={}", request.parentId());
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "親メニューが存在しません");
            }
        }

        // 2. type別バリデーション
        validateByType(request);

        // 3. 同階層 name 重複チェック
        final var count = menuRepository.existsByParentIdAndName(
                request.parentId(),
                request.name()
        );
        if (count > 0) {
            log.warn("[MenuAppService#createMenu] duplicate name parentId={}, name={}",
                    request.parentId(), request.name());
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "同一階層に同名メニューが存在します");
        }

        // 4. path 重複チェック
        if (StringUtils.hasText(request.path())) {
            final var pathCount = menuRepository.countByPath(request.path());
            if (pathCount > 0) {
                log.warn("[MenuAppService#createMenu] duplicate path path={}", request.path());
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "path が既に存在します");
            }
        }

        // 5. entity変換
        final var entity = MenuAssembler.toNewEntity(request);

        // 6. 保存
        baseMapper.insert(entity);

        log.info("[MenuAppService#createMenu] success: id={}", entity.getId());

        return entity.getId();
    }

    @Override
    @Transactional
    public void updateMenu(Long id, MenuSaveOrUpdateRequest request) {
        log.info("[MenuAppService#updateMenu] start: id={}, request={}", id, request);

        // 1. 存在チェック
        final var existing = baseMapper.selectById(id);
        if (existing == null) {
            log.warn("[MenuAppService#updateMenu] not found existing={}", id);
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "メニューが存在しません");
        }

        // 2. 自身を親に設定禁止
        if (request.parentId().equals(id)) {
            log.warn("[MenuAppService#updateMenu] invalid parentId: parentId={} equals id={}", request.parentId(), id);
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "自身を親に設定できません");
        }

        // 3. 親メニュー存在チェック
        if (request.parentId() != 0) {
            final var parent = baseMapper.selectById(request.parentId());
            if (parent == null) {
                log.warn("[MenuAppService#updateMenu] parent not found parentId={}", request.parentId());
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "親メニューが存在しません");
            }
        }

        // 4. 子孫ノードチェック（循環防止）
        checkParentNotChild(id, request.parentId());

        // 5. type変更チェック
        if (existing.getType() != request.type()) {
            final var children = menuRepository.countByParentId(id);
            if (children > 0) {
                log.warn("[MenuAppService#updateMenu] cannot change type due to existing children: id={}, childrenCount={}", id, children);
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "子メニューが存在するためタイプ変更できません");
            }
        }

        // 6. type別バリデーション
        validateByType(request);

        // 7. 重複チェック（自分除外）
        final var count = menuRepository.countByParentIdAndNameExcludeId(
                request.parentId(),
                request.name(),
                id
        );
        if (count > 0) {
            log.warn("[MenuAppService#updateMenu] duplicate name on same level: parentId={}, name={}, id={}",
                    request.parentId(), request.name(), id);
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "同一階層に同名メニューが存在します");
        }

        // 8. path 重複チェック（任意）
        if (StringUtils.hasText(request.path())) {
            final var pathCount = menuRepository.countByPathExcludeId(request.path(), id);
            if (pathCount > 0) {
                log.warn("[MenuAppService#updateMenu] duplicate path: path={}, id={}", request.path(), id);
                throw BizException.withDetail(ErrorCode.BAD_REQUEST, "path が既に存在します");
            }
        }

        // 9. entity更新
        MenuAssembler.applyToExisting(request, existing);

        // 10. 更新
        baseMapper.updateById(existing);

        log.info("[MenuAppService#updateMenu] success: id={}", id);
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) {

        if (id == null) {
            log.warn("[MenuAppService#deleteMenu] id is null");
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "メニューIDが指定されていません");
        }

        final var existing = baseMapper.selectById(id);
        if (existing == null) {
            log.warn("[MenuAppService#deleteMenu] menu not found id={}", id);
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "メニューが存在しません");
        }

        // 子メニュー存在チェック
        final var childMenuCount = menuRepository.countByParentId(id);
        if (childMenuCount > 0) {
            log.warn("[MenuAppService#deleteMenu] cannot delete menu with existing children: id={}, childCount={}", id, childMenuCount);
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "子メニューが存在するため削除できません");
        }

        // 使用中チェック
        long count = roleMenuMapper.selectCount(
                Wrappers.<RoleMenu>lambdaQuery()
                        .eq(RoleMenu::getMenuId, id)
        );
        if (count > 0) {
            log.warn("[MenuAppService#deleteMenu] menu is used by roles, cannot delete: id={}", id);
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "該当メニューは使用中のため削除できません");
        }

        //ロールメニュー関連の削除
        roleMenuMapper.delete(
                Wrappers.<RoleMenu>lambdaQuery()
                        .eq(RoleMenu::getMenuId, id)
        );
        log.info("[MenuAppService#deleteMenu] related role-menu associations deleted: menuId={}", id);

        // メニュー項目の削除
        baseMapper.deleteById(id);
        log.info("[MenuAppService#deleteMenu] menu deleted successfully: id={}", id);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Status status) {
        log.info("[MenuAppService#changeStatus] start: id={}, status={}", id, status);
        if (id == null) {
            log.warn("[MenuAppService#changeStatus] id is null");
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "メニューIDが指定されていません");
        }
        if (status == null) {
            log.warn("[MenuAppService#changeStatus] status is null");
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ステータスが指定されていません");
        }
        final var menu = baseMapper.selectById(id);
        if (menu == null) {
            log.warn("[MenuAppService#changeStatus] menu not found id={}", id);
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "メニューが存在しません");
        }
        final var oldStatus = menu.getStatus();
        if (oldStatus == status) {
            log.info("[MenuAppService#changeStatus] status is the same, no change needed: id={}, status={}", id, status);
            return;
        }
        baseMapper.update(
                null,
                Wrappers.<Menu>lambdaUpdate()
                        .set(Menu::getStatus, status)
                        .eq(Menu::getId, id)
        );
        log.info("[MenuAppService#changeStatus] status changed successfully: id={}, oldStatus={}, newStatus={}", id, oldStatus, status);
    }

    /**
     * メニュー種別ごとのバリデーション
     */
    private void validateByType(MenuSaveOrUpdateRequest request) {
        switch (request.type()) {
            case MENU -> {
                if (!StringUtils.hasText(request.path()) ||
                        !StringUtils.hasText(request.component())) {
                    log.warn("[MenuAppService#validateByType] validation failed: path/component required");
                    throw BizException.withDetail(ErrorCode.BAD_REQUEST, "メニューのパスとコンポーネントは必須です");
                }
            }
            case BUTTON -> {
                if (!StringUtils.hasText(request.permission())) {
                    log.warn("[MenuAppService#validateByType] validation failed: permission required");
                    throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ボタンは permission が必須です");
                }
            }
            case DIRECTORY -> {
                // ディレクトリは制限なし（必要なら拡張）
            }
        }
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
     * @param parentMap parentId をキー、子ノードのリストを値とするマップ
     * @param parentId 現在の親ID（最初は0からスタート）
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
