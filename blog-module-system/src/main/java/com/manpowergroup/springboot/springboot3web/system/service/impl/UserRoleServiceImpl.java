package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.entity.UserRole;
import com.manpowergroup.springboot.springboot3web.system.mapper.UserRoleMapper;
import com.manpowergroup.springboot.springboot3web.system.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * ユーザー・ロール紐付け 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    @Transactional
    public void saveOrUpdate(Long userId, Long[] roleIds) {

        // ===== 引数チェック：userId 必須 =====
        if (userId == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ユーザーIDが指定されていません。");
        }

        // ===== 引数チェック：roleIds 必須（null 不可）=====
        // null はパラメータ不備扱い、空配列は「全ロール解除」を意味する
        if (roleIds == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールID一覧が指定されていません。");
        }

        // ===== 入力ロールIDをSet化（重複排除・null除外）=====
        final Set<Long> targetRoleIds = Arrays.stream(roleIds).filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));

        final LocalDateTime now = LocalDateTime.now();

        // ===== 既存関連取得（論理削除含む）=====
        // @TableLogic の自動フィルタを回避するため、専用Mapperメソッドを使用
        final List<UserRole> existingAll = userRoleMapper.selectAllByUserIdIncludeDeleted(userId);

        // ===== roleId → UserRole マッピング生成 =====
        // 理論上、(user_id, role_id) は一意（uk_user_role）
        final Map<Long, UserRole> existingMap = existingAll.stream().collect(Collectors.toMap(UserRole::getRoleId, Function.identity(), (a, b) -> {
            // データ不整合検出ログ（通常発生しない想定）
            log.error("[UserRole.saveOrUpdate] duplicated rows: userId={}, " +
                    "roleId={}, id1={}, del1={}, id2={}, del2={}", userId, a.getRoleId(), a.getId(), a.getIsDeleted(), b.getId(), b.getIsDeleted());
            // 先に取得されたレコードを採用
            return a;
        }));

        // ===== 復活対象ロールID =====
        final Set<Long> toRestore = new HashSet<>();

        // ===== 新規登録対象レコード =====
        final List<UserRole> toInsert = new ArrayList<>();

        // ===== 追加・復活判定処理 =====
        for (Long roleId : targetRoleIds) {

            final UserRole row = existingMap.get(roleId);

            if (row == null) {
                // --- 未登録 → 新規INSERT対象 ---
                final UserRole newRow = UserRole.builder()
                        .userId(userId)
                        .roleId(roleId)
                        .isDeleted((byte) 0)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();
                toInsert.add(newRow);

            } else {
                // --- 登録済み → 論理削除状態なら復活 ---
                final Byte isDeleted = row.getIsDeleted();
                if (Byte.valueOf((byte) 1).equals(isDeleted)) {
                    toRestore.add(roleId);
                }
            }
        }

        // ===== 削除対象判定 =====
        // 現在有効(is_deleted=0) かつ 今回指定に含まれないロール
        final Set<Long> toDelete = existingAll.stream()
                .filter(r -> Byte.valueOf((byte) 0).equals(r.getIsDeleted()))
                .map(UserRole::getRoleId).filter(roleId -> !targetRoleIds.contains(roleId)).collect(Collectors.toSet());

        log.info("[UserRole.saveOrUpdate] start: userId={}, targetSize={}, existingSize={}, restoreSize={}, insertSize={}, deleteSize={}", userId, targetRoleIds.size(), existingAll.size(), toRestore.size(), toInsert.size(), toDelete.size());

        // ===== 1. 論理削除 → 復活処理 =====
        if (!toRestore.isEmpty()) {
            final int restored = userRoleMapper.restoreRoles(userId, toRestore, now);

            log.info("[UserRole.saveOrUpdate] restore executed: userId={}, count={}, roleIds={}", userId, restored, toRestore);
        }

        // ===== 2. 新規INSERT処理 =====
        if (!toInsert.isEmpty()) {
            this.saveBatch(toInsert);

            log.info(
                    "[UserRole.saveOrUpdate] insert executed: userId={}, count={}, roleIds={}",
                    userId,
                    toInsert.size(),
                    toInsert.stream().map(UserRole::getRoleId).collect(Collectors.toList())
            );
        }

        // ===== 3. 論理削除処理 =====
        if (!toDelete.isEmpty()) {
            final int deleted = userRoleMapper.logicalDeleteRoles(userId, toDelete, now);

            log.info("[UserRole.saveOrUpdate] logical delete executed: userId={}, count={}, roleIds={}", userId, deleted, toDelete);
        }

        log.info("[UserRole.saveOrUpdate] completed: userId={}", userId);
    }
}
