package com.manpowergroup.springboot.springboot3web.system.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.springboot.springboot3web.system.user.mapper.UserMapper;
import com.manpowergroup.springboot.springboot3web.system.user.model.User;
import com.manpowergroup.springboot.springboot3web.system.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {}
