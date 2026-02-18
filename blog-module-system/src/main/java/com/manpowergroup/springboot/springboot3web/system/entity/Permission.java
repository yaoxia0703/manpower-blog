package com.manpowergroup.springboot.springboot3web.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API）
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Data
@TableName("t_sys_permission")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain=true)
public class Permission {

    /**
     * 主キーID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 親権限ID（0=ルート）
     */
    private Long parentId;

    /**
     * 権限名
     */
    private String name;

    /**
     * 権限制御コード（例：user:add / article:edit）
     */
    private String code;

    /**
     * 権限種別（1=MENU, 2=BUTTON, 3=API）
     */
    private Byte type;

    /**
     * 対象パス（MENU/API 用）
     */
    private String path;

    /**
     * HTTPメソッド（API 用：GET/POST/PUT/DELETE）
     */
    private String method;

    /**
     * 表示順
     */
    private Integer sort;

    /**
     * 状態（0=無効、1=有効）
     */
    private Byte status;

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
}
