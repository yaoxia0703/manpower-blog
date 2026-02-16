package com.manpowergroup.springboot.springboot3web.system.service;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.AccountType;
import com.manpowergroup.springboot.springboot3web.system.dto.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.entity.UserAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * ユーザーログインアカウント 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface UserAccountService extends IService<UserAccount> {

    Optional<UserAccount> findActiveAccount(AccountType type, String accountValue);

    List<String> findRoleNamesByUserId(Long userId);

    Optional<LoginAccountUserDTO> findLoginUserByAccountTypeAndAccountValue(String accountType, String accountValue);

}
