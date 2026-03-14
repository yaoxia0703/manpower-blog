package com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.user;

import com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * ユーザーログインアカウント Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    LoginAccountUserDTO findLoginUserByAccountTypeAndAccountValue(
            @Param("accountType") String accountType,
            @Param("accountValue") String accountValue
    );
}
