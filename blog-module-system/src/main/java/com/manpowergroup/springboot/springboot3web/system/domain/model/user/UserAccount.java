package com.manpowergroup.springboot.springboot3web.system.domain.model.user;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.AccountType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.VerifiedStatus;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ユーザーログインアカウント
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Data
@TableName("t_sys_user_account")
@Builder
public class UserAccount {

    /**
     * 主キー（自動採番）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * ユーザーID（t_sys_user.id）
     */
    private Long userId;

    /**
     * アカウント種別（EMAIL / PHONE）
     */
    private AccountType accountType;

    /**
     * ログイン識別子（メールアドレス／電話番号）
     */
    private String accountValue;

    /**
     * ログインパスワード（ハッシュ化）
     */
    private String password;

    /**
     * 認証済みフラグ（0=未認証、1=認証済み）
     */
    private VerifiedStatus verified;

    /**
     * アカウント状態（0=無効、1=有効）
     */
    private Status status;

    /**
     * 作成日時
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 論理削除フラグ（0=未削除、1=削除済み）
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Byte isDeleted;


    public void login(User user) {
        ensureLoginAllowed(user);
    }
    public void ensureLoginAllowed(User user) {
        validateAccountStatus();
        validateVerifiedStatus();
        validatePassword();
        user.validateUserStatus();
    }

    public void validateAccountStatus() {
        if (status != null && status == Status.DISABLED) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "アカウントは無効化されています。");
        }
    }

    public void validateVerifiedStatus() {
        if (verified != null && verified == VerifiedStatus.UNVERIFIED) {
            throw BizException.withDetail(ErrorCode.FORBIDDEN, "アカウントは未認証です。");
        }
    }

    public void validatePassword() {
        if (password == null || password.isBlank()) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "パスワードが設定されていません。");
        }
    }
}
