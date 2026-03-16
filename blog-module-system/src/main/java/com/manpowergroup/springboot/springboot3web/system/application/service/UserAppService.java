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
    /**
     * ユーザーIDによりログインユーザー詳細情報を取得する
     *
     * @param userId ユーザーID
     * @return ログインユーザー詳細情報
     */
    LoginUser findLoginUserDetailByUserId(Long userId);
}
