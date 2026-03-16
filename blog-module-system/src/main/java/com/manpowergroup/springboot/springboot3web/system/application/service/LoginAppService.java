package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;

public interface LoginAppService {

    /**
     * ユーザーログイン処理
     *
     * @param req ログインリクエスト
     * @return ログインユーザー情報
     */
    LoginUser login(LoginRequest req);



}
