package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import com.manpowergroup.springboot.springboot3web.system.application.assembler.MenuAssembler;
import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.vo.MenuTreeVo;
import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.MenuRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.menu.MenuMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.MenuAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class MenuAppServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuAppService {

    private final MenuRepository menuRepository;

    public MenuAppServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<MenuTreeVo> selectAllMenus() {
        return List.of();
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

        // 4. path 重複チェック（任意だが推奨）
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
    public void deleteMenu(Long id) {

    }

    @Override
    public void changeStatus(Long id, Status status) {

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
}
