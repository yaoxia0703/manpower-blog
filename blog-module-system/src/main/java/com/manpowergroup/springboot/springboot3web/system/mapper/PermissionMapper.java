package com.manpowergroup.springboot.springboot3web.system.mapper;

import com.manpowergroup.springboot.springboot3web.system.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API） Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);

}
