package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.system.application.dto.permission.PermissionQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.permission.PermissionSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.domain.model.permission.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API） 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface PermissionAppService extends IService<Permission> {

    List<String> selectPermissionCodesByUserId(Long userId);

    JoinPageResult<Permission> pagePermission(PermissionQueryRequest queryRequest, PageRequest pageRequest);

    Permission getPermissionById(Long id);

    Long createPermission(PermissionSaveOrUpdateRequest request);

    void updatePermission(Long id, PermissionSaveOrUpdateRequest request);

    void deletePermission(Long id);

    void changeStatus(Long id, Status status);


}
