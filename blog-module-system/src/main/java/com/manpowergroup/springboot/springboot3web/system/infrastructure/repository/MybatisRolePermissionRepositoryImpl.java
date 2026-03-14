package com.manpowergroup.springboot.springboot3web.system.infrastructure.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RolePermission;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.RolePermissionRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role.RolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MybatisRolePermissionRepositoryImpl implements RolePermissionRepository {

     private final RolePermissionMapper rolePermissionMapper;

     @Override
     public List<RolePermission> selectAllByRoleIdIncludeDeleted(Long roleId) {
         return rolePermissionMapper.selectAllByRoleIdIncludeDeleted(roleId);
     }

     @Override
     public int restorePermissions(Long roleId, Collection<Long> permissionIds, LocalDateTime now) {
         return rolePermissionMapper.restorePermissions(roleId, permissionIds, now);
     }

     @Override
     public int logicalDeletePermissions(Long roleId, Collection<Long> permissionIds, LocalDateTime now) {
         return rolePermissionMapper.logicalDeletePermissions(roleId, permissionIds, now);
     }
}
