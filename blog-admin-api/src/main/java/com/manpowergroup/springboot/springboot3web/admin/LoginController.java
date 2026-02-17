package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginResponse;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.framework.security.jwt.JwtTokenProvider;
import com.manpowergroup.springboot.springboot3web.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.springboot.springboot3web.system.service.LoginService;
import com.manpowergroup.springboot.springboot3web.system.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/auth")
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public Result<LoginResponse<LoginUser>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        LoginUser loginUser = loginService.login(loginRequest);

        String token = jwtTokenProvider.generateToken(loginUser);

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return Result.ok(
                LoginResponse.<LoginUser>builder()
                        .accessToken(token)
                        .user(loginUser)
                        .build()
        );
    }


    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.ok(null);
    }

    @GetMapping("/me")
    public Result<LoginUser> me() {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        final Object principal = auth.getPrincipal();

        final Long userId;
        if (principal instanceof LoginPrincipal p) {
            userId = p.userId();
        } else if (principal instanceof Long id) {
            // 互換用（旧実装が principal=Long の場合）
            userId = id;
        } else {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        final LoginUser loginUser = userService.findLoginUserDetailByUserId(userId);
        if (loginUser == null) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        return Result.ok(loginUser);
    }




}
