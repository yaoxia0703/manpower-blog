package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface UserAppService extends IService<User> {
    public LoginUser findLoginUserDetailByUserId(Long userId);
}
