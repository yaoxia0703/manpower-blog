package com.manpowergroup.springboot.springboot3web.system.infrastructure.repository;

import com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.UserAccountRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.user.UserAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MybatisUserAccountRepositoryImpl implements UserAccountRepository {
    private final UserAccountMapper userAccountMapper;

    @Override
    public LoginAccountUserDTO findLoginUserByAccountTypeAndAccountValue(String accountType, String accountValue) {
        return userAccountMapper.findLoginUserByAccountTypeAndAccountValue(accountType, accountValue);
    }
}
