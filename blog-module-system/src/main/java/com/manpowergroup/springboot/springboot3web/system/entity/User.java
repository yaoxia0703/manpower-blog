package com.manpowergroup.springboot.springboot3web.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * システムユーザー
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Getter
@Setter
@TableName("t_sys_user")
@Schema(description = "システムユーザー")
public class User {

    /**
     * ユーザーID
     */
    @Schema(description = "ユーザーID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * ログインユーザー名（システム内部識別子）
     */
    @Schema(description = "ログインユーザー名（システム内部識別子）")
    private String username;

    /**
     * ニックネーム
     */
    @Schema(description = "ニックネーム")
    private String nickName;

    /**
     * ユーザー状態（0=無効、1=有効）
     */
    @Schema(description = "ユーザー状態（0=無効、1=有効）")
    private Integer status;

    /**
     * 作成日時
     */
    @Schema(description = "作成日時")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Schema(description = "更新日時")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 論理削除フラグ（0=未削除、1=削除済み）
     */
    @Schema(description = "論理削除フラグ（0=未削除、1=削除済み）")
    @TableLogic
    private Integer isDeleted;
}
