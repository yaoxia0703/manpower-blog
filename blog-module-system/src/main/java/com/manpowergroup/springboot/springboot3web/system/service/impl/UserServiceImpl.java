package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.manpowergroup.springboot.springboot3web.system.entity.User;
import com.manpowergroup.springboot.springboot3web.system.mapper.UserMapper;
import com.manpowergroup.springboot.springboot3web.system.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
