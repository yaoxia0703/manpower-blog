package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.User;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.user.UserMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.UserAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Service
public class UserAppServiceImpl extends ServiceImpl<UserMapper, User> implements UserAppService {

    private final UserMapper userMapper;

    public UserAppServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public LoginUser findLoginUserDetailByUserId(Long userId) {
        return userMapper.selectLoginUserDetailByUserId(userId);
    }


}
