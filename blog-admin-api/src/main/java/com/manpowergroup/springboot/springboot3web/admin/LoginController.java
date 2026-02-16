package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.framework.security.jwt.JwtTokenProvider;
import com.manpowergroup.springboot.springboot3web.system.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/auth")
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public Result<LoginUser> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {

        log.info("Login attempt: accountType={}, accountValue={}",
                loginRequest.getAccountType(),
                loginRequest.getAccountValue());

        LoginUser loginUser = loginService.login(loginRequest);

        String token = jwtTokenProvider.generateToken(loginUser);

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        log.info("Login success: userId={}", loginUser.getUserId());

        return Result.ok(loginUser);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.ok(null);
    }

    @GetMapping("/me")
    public Result<LoginUser> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        Long userId;
        try {
            userId = (Long) auth.getPrincipal();
        } catch (ClassCastException e) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        LoginUser loginUser = LoginUser.builder()
                .userId(userId)
                .build();

        return Result.ok(loginUser);
    }
}
