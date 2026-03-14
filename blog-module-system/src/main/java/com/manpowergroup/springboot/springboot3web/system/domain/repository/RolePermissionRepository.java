package com.manpowergroup.springboot.springboot3web.system.domain.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RolePermission;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface RolePermissionRepository {

    List<RolePermission> selectAllByRoleIdIncludeDeleted( Long roleId);

    int restorePermissions( Long roleId,  Collection<Long> permissionIds,  LocalDateTime now);

    int logicalDeletePermissions( Long roleId,  Collection<Long> permissionIds, LocalDateTime now);
}
