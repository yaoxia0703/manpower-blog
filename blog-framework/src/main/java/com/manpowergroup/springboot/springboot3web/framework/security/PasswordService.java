package com.manpowergroup.springboot.springboot3web.framework.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 明文密码加密（注册/修改密码用）
     */
    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 明文与数据库密文匹配（登录用）
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
