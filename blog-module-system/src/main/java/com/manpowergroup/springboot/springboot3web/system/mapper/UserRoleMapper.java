package com.manpowergroup.springboot.springboot3web.system.mapper;

import com.manpowergroup.springboot.springboot3web.system.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * ユーザー・ロール紐付け Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
