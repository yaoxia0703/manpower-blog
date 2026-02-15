package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.entity.UserAccount;
import com.manpowergroup.springboot.springboot3web.system.service.LoginService;
import com.manpowergroup.springboot.springboot3web.system.service.UserAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SubservienceImpl implements LoginService {
    private UserAccountService userAccountService;


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
        if (account.getStatus() == 0) {
            log.warn("Login failed: account is disabled. accountValue={}", AccountValue);
            throw new BizException(ErrorCode.UNAUTHORIZED, "アカウントは無効化されています。");
        }
        if (account.getVerified() != null && account.getVerified() == 0) {
            throw new BizException(ErrorCode.FORBIDDEN, "アカウントは未認証です。");
        }
        List<String> roleNames = userAccountService.findRoleNamesByUserId(account.getUserId());

        // 5) 返回 LoginUser（不做 session）
        return LoginUser.builder()
                .userId(account.getUserId())
                .accountId(account.getId())
                .accountType(req.getAccountType())
                .accountValue(req.getAccountValue())
                .roleNames(roleNames)
                .build();
    }
}
