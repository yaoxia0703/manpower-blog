package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.manpowergroup.springboot.springboot3web.system.entity.Permission;
import com.manpowergroup.springboot.springboot3web.system.mapper.PermissionMapper;
import com.manpowergroup.springboot.springboot3web.system.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API） 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
