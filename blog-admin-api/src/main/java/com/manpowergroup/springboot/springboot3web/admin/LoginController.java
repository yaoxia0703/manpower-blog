package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginResponse;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.framework.security.jwt.JwtTokenProvider;
import com.manpowergroup.springboot.springboot3web.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.springboot.springboot3web.system.application.dto.me.MeResponse;
import com.manpowergroup.springboot.springboot3web.system.application.service.LoginAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.MenuAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.PermissionAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.UserAppService;
import com.manpowergroup.springboot.springboot3web.system.application.vo.MenuTreeVo;
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

import java.util.List;

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
    private final UserAppService userAppService;
    private final MenuAppService menuAppService;
    private final PermissionAppService permissionAppService;

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
    public Result<MeResponse> me() {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        if (!(auth.getPrincipal() instanceof LoginPrincipal p)) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        final Long userId = p.userId();
        final Long accountId = p.accountId();

        // 1. 用户信息
        final LoginUser loginUser = userAppService.getCurrentUserContext(userId, accountId);

        if (loginUser == null) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーはログインしていません。");
        }

        // 2. menu
        final List<MenuTreeVo> menus = menuAppService.selectMenusByUserId(userId);

        // 3. permission
        final List<String> permissions = permissionAppService.selectPermissionCodesByUserId(userId);

        // 4. 组装返回
        final MeResponse response = MeResponse.builder()
                .user(loginUser)
                .menus(menus)
                .permissions(permissions)
                .build();

        return Result.ok(response);
    }


}
