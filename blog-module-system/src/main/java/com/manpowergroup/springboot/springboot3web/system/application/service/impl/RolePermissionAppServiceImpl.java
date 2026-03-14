package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RolePermission;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.RolePermissionRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role.RolePermissionMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.RolePermissionAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * ロール・権限紐付け 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RolePermissionAppServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionAppService {

    private final RolePermissionRepository rolePermissionRepository;


    @Override
    @Transactional
    public void saveOrUpdate(Long roleId, Long[] permissionIds) {

        // ===== 引数チェック：roleId 必須 =====
        if (roleId == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }

        // ===== 引数チェック：permissionIds 必須（null 不可）=====
        // null はパラメータ不備扱い、空配列は「全権限解除」を意味する
        if (permissionIds == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "権限ID一覧が指定されていません。");
        }

        // ===== 入力権限IDをSet化（重複排除・null除外）=====
        final Set<Long> targetPermissionIds = Arrays.stream(permissionIds)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        final LocalDateTime now = LocalDateTime.now();

        // ===== 既存関連取得（論理削除含む）=====
        final List<RolePermission> existingAll =
                this.rolePermissionRepository.selectAllByRoleIdIncludeDeleted(roleId);

        // ===== permissionId → RolePermission マッピング生成 =====
        final Map<Long, RolePermission> existingMap = existingAll.stream()
                .collect(Collectors.toMap(
                        RolePermission::getPermissionId,
                        Function.identity(),
                        (e1, e2) -> {
                            log.error(
                                    "[RolePermission.saveOrUpdate] duplicated rows detected: roleId={}, permissionId={}, id1={}, del1={}, id2={}, del2={}",
                                    e1.getRoleId(),
                                    e1.getPermissionId(),
                                    e1.getId(),
                                    e1.getIsDeleted(),
                                    e2.getId(),
                                    e2.getIsDeleted()
                            );
                            return e1;
                        }
                ));

        // ===== 復活対象permissionId / 新規INSERT対象 =====
        final Set<Long> toRestore = new HashSet<>();
        final List<RolePermission> toInsert = new ArrayList<>();

        for (Long permissionId : targetPermissionIds) {
            final RolePermission row = existingMap.get(permissionId);

            if (row == null) {
                // --- 未登録 → 新規INSERT対象 ---
                final RolePermission newRow = RolePermission.builder()
                        .roleId(roleId)
                        .permissionId(permissionId)
                        .isDeleted((byte) 0)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();
                toInsert.add(newRow);

            } else {
                // --- 登録済み → 論理削除状態なら復活 ---
                if (Byte.valueOf((byte) 1).equals(row.getIsDeleted())) {
                    toRestore.add(permissionId);
                }
            }
        }

        // ===== 論理削除対象（現在有効かつ今回指定に含まれない）=====
        final Set<Long> toDelete = existingAll.stream()
                .filter(r -> Byte.valueOf((byte) 0).equals(r.getIsDeleted()))
                .map(RolePermission::getPermissionId)
                .filter(permissionId -> !targetPermissionIds.contains(permissionId))
                .collect(Collectors.toSet());

        log.info("[RolePermission.saveOrUpdate] start: roleId={}, targetSize={}, existingSize={}, restoreSize={}, insertSize={}, deleteSize={}",
                roleId, targetPermissionIds.size(), existingAll.size(), toRestore.size(), toInsert.size(), toDelete.size());

        // 1) 復活（is_deleted=1 -> 0）
        if (!toRestore.isEmpty()) {
            final int restored = rolePermissionRepository.restorePermissions(roleId, toRestore, now);
            log.info("[RolePermission.saveOrUpdate] restore executed: roleId={}, count={}, permissionIds={}", roleId, restored, toRestore);
        }

        // 2) 新規INSERT
        if (!toInsert.isEmpty()) {
            this.saveBatch(toInsert);
            log.info("[RolePermission.saveOrUpdate] insert executed: roleId={}, count={}, permissionIds={}",
                    roleId,
                    toInsert.size(),
                    toInsert.stream().map(RolePermission::getPermissionId).collect(Collectors.toList())
            );
        }

        // 3) 論理削除（is_deleted=0 -> 1）
        if (!toDelete.isEmpty()) {
            final int deleted = rolePermissionRepository.logicalDeletePermissions(roleId, toDelete, now);
            log.info("[RolePermission.saveOrUpdate] logical delete executed: roleId={}, count={}, permissionIds={}", roleId, deleted, toDelete);
        }

        log.info("[RolePermission.saveOrUpdate] completed: roleId={}", roleId);
    }
}
