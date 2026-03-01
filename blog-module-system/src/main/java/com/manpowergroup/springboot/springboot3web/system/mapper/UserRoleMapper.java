package com.manpowergroup.springboot.springboot3web.system.mapper;

import com.manpowergroup.springboot.springboot3web.system.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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

    List<UserRole> selectAllByUserIdIncludeDeleted(@Param("userId") Long userId);

    int restoreRoles(@Param("userId") Long userId, @Param("roleIds") Collection<Long> roleIds, @Param("now") LocalDateTime now);

    int logicalDeleteRoles(
            @Param("userId") Long userId,
            @Param("roleIds") Collection<Long> roleIds,
            @Param("now") LocalDateTime now
    );

}
