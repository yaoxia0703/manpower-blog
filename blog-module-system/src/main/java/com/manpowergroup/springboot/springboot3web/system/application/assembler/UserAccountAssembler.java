package com.manpowergroup.springboot.springboot3web.system.application.assembler;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.UserErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.VerifiedStatus;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserAccount;
import lombok.extern.slf4j.Slf4j;

/**
 * UserAccountAssemblerは、LoginAccountUserDTOからUserAccountエンティティへの変換を担当するクラスです。
 * ログイン処理において、データベースから取得したアカウント情報をUserAccountエンティティにマッピングするために使用されます。
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Slf4j
public final class UserAccountAssembler {
    private UserAccountAssembler() {
    }

    public static UserAccount toEntity(LoginAccountUserDTO dto) {
        if(dto.getAccountId() == null){
            log.warn("[UserAccountAssembler#toEntity] failed: accountId is null. ");
            throw  BizException.withDetail(UserErrorCode.INVALID_ACCOUNT_DATA,"ユーザーIDが存在しません。");
        }
        if(dto.getAccountStatus() == null){
            log.warn("[UserAccountAssembler#toEntity] failed: accountStatus is null. accountId={}", dto.getAccountId());
            throw  BizException.withDetail(UserErrorCode.INVALID_ACCOUNT_DATA,"アカウントステータスが存在しません。");
        }

        return UserAccount.builder()
                .id(dto.getAccountId())
                .status(dto.getAccountStatus())
                .verified(dto.getVerified()!= null ? dto.getVerified() : VerifiedStatus.UNVERIFIED)
                .password(dto.getPassword())
                .build();
    }
}
