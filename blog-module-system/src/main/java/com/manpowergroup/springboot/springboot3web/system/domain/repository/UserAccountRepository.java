package com.manpowergroup.springboot.springboot3web.system.domain.repository;

import com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount.LoginAccountUserDTO;

public interface UserAccountRepository {
    LoginAccountUserDTO findLoginUserByAccountTypeAndAccountValue(String accountType, String accountValue);
}
