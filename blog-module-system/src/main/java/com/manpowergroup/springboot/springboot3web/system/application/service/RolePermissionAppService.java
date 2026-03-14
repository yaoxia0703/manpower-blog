package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ロール・権限紐付け 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface RolePermissionAppService extends IService<RolePermission> {

    void saveOrUpdate(Long roleId, Long[] permissionIds);

}
