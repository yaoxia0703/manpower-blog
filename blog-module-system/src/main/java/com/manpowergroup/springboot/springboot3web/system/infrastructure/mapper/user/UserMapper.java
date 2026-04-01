package com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.user;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 指定ユーザーIDによりログインユーザー詳細情報を取得する
     *
     * @param userId ユーザーID
     * @return ログインユーザー詳細情報
     */
    LoginUser getCurrentUserContext(@Param("userId") Long userId,@Param("accountId") Long accountId);



}
