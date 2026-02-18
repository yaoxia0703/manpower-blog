package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.framework.security.PasswordService;
import com.manpowergroup.springboot.springboot3web.system.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.service.LoginService;
import com.manpowergroup.springboot.springboot3web.system.service.UserAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private UserAccountService userAccountService;
    private PasswordService passwordService;


    @Override
    public LoginUser login(LoginRequest req) {

        final var accountValue = req.getAccountValue();
        final var accountType = req.getAccountType();

        LoginAccountUserDTO dto = userAccountService
                .findLoginUserByAccountTypeAndAccountValue(accountType.toString(), accountValue)
                .orElseThrow(() -> {
                    log.warn("Login failed: account not found. accountType={}, accountValue={}", accountType, accountValue);
                    return BizException.withDetail(
                            ErrorCode.UNAUTHORIZED,
                            "accountType=" + accountType + ", accountValue=" + accountValue
                    );
                });

        if (dto.getUserStatus() != null && dto.getUserStatus() == 0) {
            log.warn("Login failed: user is disabled. accountType={}, accountValue={}, userId={}",
                    accountType, accountValue, dto.getUserId());
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "ユーザーは無効化されています。");
        }

        if (dto.getAccountStatus() != null && dto.getAccountStatus() == 0) {
            log.warn("Login failed: account is disabled. accountType={}, accountValue={}, accountId={}",
                    accountType, accountValue, dto.getAccountId());
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "アカウントは無効化されています。");
        }

        if (dto.getVerified() != null && dto.getVerified() == 0) {
            log.warn("Login failed: account not verified. accountType={}, accountValue={}, accountId={}",
                    accountType, accountValue, dto.getAccountId());
            throw BizException.withDetail(ErrorCode.FORBIDDEN, "アカウントは未認証です。");
        }

        // 5) 密码照合（BCrypt）
        if (dto.getPassword() == null || dto.getPassword().isBlank()
                || !passwordService.matches(req.getPassword(), dto.getPassword())) {
            log.warn("Login failed: password mismatch. accountType={}, accountValue={}", accountType, accountValue);
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "パスワードが正しくありません。");
        }

        List<String> roleNames = userAccountService.findRoleNamesByUserId(dto.getUserId());

        return LoginUser.builder()
                .userId(dto.getUserId())
                .accountId(dto.getAccountId())
                .accountType(req.getAccountType())
                .accountValue(req.getAccountValue())
                .roleNames(roleNames)
                .nickName(dto.getNickName())
                .build();
    }



}
