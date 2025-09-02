package com.manpowergroup.springboot.springboot3web.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.springboot.springboot3web.system.user.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}
