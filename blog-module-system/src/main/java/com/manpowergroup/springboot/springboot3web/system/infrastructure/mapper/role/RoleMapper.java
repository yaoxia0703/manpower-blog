package com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role;

import com.manpowergroup.springboot.springboot3web.system.domain.model.role.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * ロールマスタ Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
