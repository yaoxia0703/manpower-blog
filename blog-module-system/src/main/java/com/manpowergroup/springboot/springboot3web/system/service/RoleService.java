package com.manpowergroup.springboot.springboot3web.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.system.dto.role.RoleQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.dto.role.RoleSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ロールマスタ 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface RoleService extends IService<Role> {
    JoinPageResult<Role> pageRoles(PageRequest pageRequest, RoleQueryRequest query);


    Role getRoleById(Long id);

    Long createRole(RoleSaveOrUpdateRequest request);

    void updateRole(Long id, RoleSaveOrUpdateRequest request);

    void deleteRole(Long id);

    void changeStatus(Long id, Status status);

}
