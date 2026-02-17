package com.manpowergroup.springboot.springboot3web.system.service;

import com.manpowergroup.springboot.springboot3web.system.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API） 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface PermissionService extends IService<Permission> {

    List<String> selectPermissionCodesByUserId(Long userId);

}
