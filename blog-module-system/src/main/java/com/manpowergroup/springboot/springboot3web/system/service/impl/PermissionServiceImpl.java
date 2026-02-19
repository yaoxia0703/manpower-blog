package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.system.dto.permission.PermissionQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.dto.permission.PermissionSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.dto.permission.PermissionStatusUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.entity.Permission;
import com.manpowergroup.springboot.springboot3web.system.mapper.PermissionMapper;
import com.manpowergroup.springboot.springboot3web.system.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API） 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Service
@AllArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    private final  PermissionMapper permissionMapper;

    @Override
    public List<String> selectPermissionCodesByUserId(Long userId) {
        return permissionMapper.selectPermissionCodesByUserId(userId);
    }

    @Override
    public JoinPageResult<Permission> pagePermission(PermissionQueryRequest queryRequest, PageRequest pageRequest) {
        return null;
    }

    @Override
    public Permission getPermissionById(Long id) {
        return null;
    }

    @Override
    public Long CreatePermission(Permission permission) {
        return 0L;
    }

    @Override
    public void updatePermission(Long id, PermissionSaveOrUpdateRequest request) {

    }

    @Override
    public void deletePermission(Long id) {

    }

    @Override
    public void changeStatus(Long id, PermissionStatusUpdateRequest request) {

    }

    private  void validateSaveOrUpdateRequest(){

    }

    private void validateStatus(PermissionStatusUpdateRequest request){

    }
}
