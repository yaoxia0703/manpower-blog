package com.manpowergroup.springboot.springboot3web.system.mapper;

import com.manpowergroup.springboot.springboot3web.system.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.entity.UserAccount;
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
