package com.manpowergroup.springboot.springboot3web.system.mapper;

import com.manpowergroup.springboot.springboot3web.system.entity.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * ロール・権限紐付け Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<RolePermission> selectAllByRoleIdIncludeDeleted(@Param("roleId") Long roleId);

    int restorePermissions(@Param("roleId") Long roleId, @Param("permissionIds") Collection<Long> permissionIds, @Param("now") LocalDateTime now);

    int logicalDeletePermissions(@Param("roleId") Long roleId, @Param("permissionIds") Collection<Long> permissionIds, @Param("now") LocalDateTime now);

}
