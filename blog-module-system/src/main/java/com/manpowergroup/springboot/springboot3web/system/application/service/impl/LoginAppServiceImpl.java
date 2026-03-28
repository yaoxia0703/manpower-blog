package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.framework.security.PasswordService;
import com.manpowergroup.springboot.springboot3web.system.application.assembler.UserAccountAssembler;
import com.manpowergroup.springboot.springboot3web.system.application.assembler.UserAssembler;
import com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.application.service.LoginAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.UserAccountAppService;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.User;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserAccount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LoginAppServiceImpl implements LoginAppService {
    private UserAccountAppService userAccountService;
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


        //2. dto-->entity 変換
        //userとuserAccountは別テーブルなので、DTOからそれぞれのEntityに変換する必要がある
        User user = UserAssembler.toEntity(dto);
        UserAccount userAccount = UserAccountAssembler.toEntity(dto);

        // 3. ユーザー状態・アカウント状態・認証状態のチェック Damain層での状態チェック
        user.ensureLoginAllowed();
        userAccount.login(user);
        // passwordの平文とDBのハッシュ化されたpasswordをBCryptで照合する service側で処理
        if (!passwordService.matches(req.getPassword(), userAccount.getPassword())) {
            log.warn("Login failed: password mismatch. accountType={}, accountValue={}", accountType, accountValue);
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "パスワードが正しくありません。");
        }
        // 4. ロール情報の取得
        List<String> roleNames = userAccountService.findRoleNamesByUserId(user.getId());

        return LoginUser.builder()
                .userId(user.getId())
                .accountId(userAccount.getId())
                .accountType(req.getAccountType())
                .accountValue(req.getAccountValue())
                .roleNames(roleNames)
                .nickName(dto.getNickName())
                .build();
    }


}
