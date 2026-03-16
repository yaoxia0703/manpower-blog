package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.AccountType;
import com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserAccount;
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
public interface UserAccountAppService extends IService<UserAccount> {

    /**
     * 有効なユーザーアカウント情報を取得する
     *
     * @param type アカウント種別（例：USERNAME / EMAIL / PHONE など）
     * @param accountValue アカウント値（ユーザー名やメールアドレス等）
     * @return 有効なユーザーアカウント（存在しない場合は Optional.empty）
     */
    Optional<UserAccount> findActiveAccount(AccountType type, String accountValue);

    /**
     * 指定ユーザーに紐づくロール名一覧を取得する
     *
     * @param userId ユーザーID
     * @return ロール名一覧
     */
    List<String> findRoleNamesByUserId(Long userId);

    /**
     * アカウント種別とアカウント値によりログインユーザー情報を取得する
     *
     * @param accountType アカウント種別（例：USERNAME / EMAIL / PHONE など）
     * @param accountValue アカウント値
     * @return ログインユーザー情報（存在しない場合は Optional.empty）
     */
    Optional<LoginAccountUserDTO> findLoginUserByAccountTypeAndAccountValue(String accountType, String accountValue);

}
