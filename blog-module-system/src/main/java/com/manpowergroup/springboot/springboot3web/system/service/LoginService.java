package com.manpowergroup.springboot.springboot3web.system.service;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;

public interface LoginService {

    LoginUser login(LoginRequest req);

}
