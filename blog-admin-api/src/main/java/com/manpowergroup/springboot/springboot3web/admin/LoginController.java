package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginResponse;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.framework.security.jwt.JwtTokenProvider;
import com.manpowergroup.springboot.springboot3web.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.springboot.springboot3web.system.application.service.LoginAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.UserAppService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * ログイン関連のAPIコントローラー
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/api/system/auth")
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final LoginAppService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAppService userService;

    /**
     * ログイン処理
     *
     * @param loginRequest ログインリクエスト
     * @param response     HTTPレスポンス（JWTトークンをヘッダーに設定するため）
     * @return ログインユーザー情報とアクセストークンを含むレスポンス
     */
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


    /**
     * ログアウト処理
     *
     * @param request HTTPリクエスト（セッションを無効化するため）
     * @return ログアウト成功のレスポンス
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        // JWTトークンはクライアント側で管理されるため、サーバー側でのトークン無効化は行わない。
        // ただし、セッションが存在する場合は無効化する。
        HttpSession session = request.getSession(false);
        // セッションが存在する場合は無効化する（JWTトークンはクライアント側で管理されるため、サーバー側でのトークン無効化は行わない）
        if (session != null) {
            // セッションを無効化する
            session.invalidate();
        }
        // セキュリティコンテキストをクリアする
        SecurityContextHolder.clearContext();
        return Result.ok();
    }


    /**
     * ログインユーザー情報の取得
     *
     * @return ログインユーザー情報
     */
    @GetMapping("/me")
    public Result<LoginUser> me() {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        final Object principal = auth.getPrincipal();

        final Long userId;
        final Long accountId;
        if (principal instanceof LoginPrincipal p) {
            userId = p.userId();
            accountId = p.accountId();
        } else if (principal instanceof Long id) {
            // 互換用（旧実装が principal=Long の場合）
            userId = id;
            accountId = null;
        } else {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        final LoginUser loginUser = userService.getCurrentUserContext(userId,accountId);
        if (loginUser == null) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        return Result.ok(loginUser);
    }


}
