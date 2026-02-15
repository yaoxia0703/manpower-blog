package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.framework.security.PasswordService;
import com.manpowergroup.springboot.springboot3web.system.entity.User;
import com.manpowergroup.springboot.springboot3web.system.entity.UserAccount;
import com.manpowergroup.springboot.springboot3web.system.service.LoginService;
import com.manpowergroup.springboot.springboot3web.system.service.UserAccountService;
import com.manpowergroup.springboot.springboot3web.system.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private UserAccountService userAccountService;
    private PasswordService passwordService;
    private UserService userService;


    @Override
    public LoginUser login(LoginRequest req) {
        final var AccountValue = req.getAccountValue();
        final var AccountType = req.getAccountType();
        UserAccount account = userAccountService.findActiveAccount(AccountType, AccountValue)
                .orElseThrow(() -> {
                    log.warn("Login failed: account not found. accountValue={}", AccountValue);
                    return BizException.withDetail(
                            ErrorCode.UNAUTHORIZED,
                            "accountType=" + AccountType + ", accountValue=" + AccountValue
                    );

                });
        User user = Optional.ofNullable(userService.lambdaQuery().eq(User::getId, account.getUserId()).one())
                .orElseThrow(() -> {
                    log.warn("Login failed: user not found for account. accountValue={}, userId={}", AccountValue, account.getUserId());
                    return BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーが見つかりません。");
                });
        if (account.getStatus() == 0) {
            log.warn("Login failed: account is disabled. accountValue={}", AccountValue);
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "アカウントは無効化されています。");
        }
        if (account.getVerified() != null && account.getVerified() == 0) {
            throw BizException.withDetail(ErrorCode.FORBIDDEN, "アカウントは未認証です。");
        }
        if (!passwordService.matches(req.getPassword(), account.getPassword())) {
            log.warn("Login failed: password mismatch. accountValue={}", AccountValue);
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "パスワードが正しくありません。");
        }
        List<String> roleNames = userAccountService.findRoleNamesByUserId(account.getUserId());

        // 5) 返回 LoginUser（不做 session）
        return LoginUser.builder()
                .userId(account.getUserId())
                .accountId(account.getId())
                .accountType(req.getAccountType())
                .accountValue(req.getAccountValue())
                .roleNames(roleNames)
                .username(user.getUsername())
                .build();
    }
}
