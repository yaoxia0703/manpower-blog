package com.manpowergroup.springboot.springboot3web.system.domain.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RolePermission;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface RolePermissionRepository {

    /**
     * 指定ロールに紐づく権限関連を取得する（論理削除されたデータも含む）
     *
     * @param roleId ロールID
     * @return ロール権限関連一覧
     */
    List<RolePermission> selectAllByRoleIdIncludeDeleted(Long roleId);

    /**
     * 論理削除されたロール権限関連を復元する
     *
     * @param roleId ロールID
     * @param permissionIds 復元対象の権限ID一覧
     * @param now 更新日時
     * @return 更新件数
     */
    int restorePermissions(Long roleId, Collection<Long> permissionIds, LocalDateTime now);

    /**
     * 指定ロールに紐づく権限関連を論理削除する
     *
     * @param roleId ロールID
     * @param permissionIds 削除対象の権限ID一覧
     * @param now 更新日時
     * @return 更新件数
     */
    int logicalDeletePermissions(Long roleId, Collection<Long> permissionIds, LocalDateTime now);
}
