package com.manpowergroup.springboot.springboot3web.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * ユーザーログインアカウント
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Data
@TableName("t_sys_user_account")
@Schema(description = "ユーザーログインアカウント")
public class UserAccount {

    @Schema(description = "アカウントID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "ユーザーID（t_sys_user.id）")
    private Long userId;

    @Schema(description = "アカウント種別（EMAIL / PHONE）")
    private AccountType accountType;

    @Schema(description = "ログイン識別子（メールアドレス／電話番号）")
    private String accountValue;

    @Schema(description = "ログインパスワード（ハッシュ化）")
    private String password;

    @Schema(description = "認証済みフラグ（0=未認証、1=認証済み）")
    private Integer verified;

    @Schema(description = "アカウント状態（0=無効、1=有効）")
    private Integer status;

    @Schema(description = "作成日時")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(description = "更新日時")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(description = "論理削除フラグ（0=未削除、1=削除済み）")
    @TableLogic
    private Integer isDeleted;
}
