package com.manpowergroup.springboot.springboot3web.system.application.assembler;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.UserErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.User;
import lombok.extern.slf4j.Slf4j;

/**
 * UserAssemblerは、LoginAccountUserDTOからUserエンティティへの変換を担当するクラスです。
 * ログイン処理において、データベースから取得したユーザー情報をUserエンティティにマッピングするために使用されます。
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Slf4j
public final class UserAssembler {
    private UserAssembler() {
    }

    public static User toEntity(LoginAccountUserDTO dto) {
        if (dto.getUserId() == null) {
            log.warn("[UserAssembler#toEntity] failed: userId is null");
            throw  BizException.withDetail(UserErrorCode.INVALID_ACCOUNT_DATA, "ユーザーIDがnullです。");
        }
        return User.builder()
                .id(dto.getUserId())
                .status(dto.getUserStatus())
                .build();
    }
}
