package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.system.application.dto.role.RoleQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.role.RoleSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ロールマスタ 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface RoleAppService extends IService<Role> {
    JoinPageResult<Role> pageRoles(PageRequest pageRequest, RoleQueryRequest query);


    Role getRoleById(Long id);

    Long createRole(RoleSaveOrUpdateRequest request);

    void updateRole(Long id, RoleSaveOrUpdateRequest request);

    void deleteRole(Long id);

    void changeStatus(Long id, Status status);

}
