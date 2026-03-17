package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RoleMenu;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role.RoleMenuMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.RoleAppMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * <p>
 * ロールメニュー関連テーブル 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@Service
@Slf4j
public class RoleMenuAppServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleAppMenuService {


    @Override
    @Transactional
    public void saveRoleMenu(Long roleId, Long[] menuIds) {

        // 処理開始ログ
        log.info("[RoleAppMenuService#saveRoleMenu] start  roleId={}, menuIds={}", roleId, Arrays.toString(menuIds));

        // ロールIDが未指定の場合はエラー
        if (roleId == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }

        // ==============================
        // 既存のロールメニュー関連を削除
        // ==============================

        log.info("[RoleAppMenuService#saveRoleMenu] 既存ロールメニュー削除 roleId={}", roleId);

        // roleId に紐づく既存のロールメニュー関連を検索条件として設定
        LambdaQueryWrapper<RoleMenu> qw = new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getRoleId, roleId);

        // 該当するロールメニュー関連を削除
        baseMapper.delete(qw);

        log.info("[RoleAppMenuService#saveRoleMenu] 既存ロールメニュー削除完了 roleId={}", roleId);

        // ==============================
        // menuIds のバリデーション
        // ==============================

        // menuIds が null または空の場合は処理不可
        if (menuIds == null || menuIds.length == 0) {
            log.warn("[RoleAppMenuService#saveRoleMenu] メニューIDが指定されていません roleId={}", roleId);
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "メニューIDが指定されていません。");
        }

        // ==============================
        // 新しいロールメニュー関連を登録
        // ==============================

        log.info("[RoleAppMenuService#saveRoleMenu] 新規ロールメニュー登録開始 roleId={}", roleId);

        // menuIds をループしてロールメニュー関連を作成
        for (Long menuId : menuIds) {

            // RoleMenu エンティティを作成
            RoleMenu roleMenu = RoleMenu.builder()
                    .roleId(roleId)
                    .menuId(menuId)
                    .build();

            // DBに保存
            baseMapper.insert(roleMenu);
        }

        // 処理完了ログ
        log.info("[RoleAppMenuService#saveRoleMenu] 新規ロールメニュー登録完了 roleId={}, menuIds={}",
                roleId, Arrays.toString(menuIds));
    }
}
