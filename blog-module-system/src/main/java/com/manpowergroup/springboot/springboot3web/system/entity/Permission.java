package com.manpowergroup.springboot.springboot3web.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.HttpMethod;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.PermissionType;

import java.time.LocalDateTime;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 権限マスタ（MENU/BUTTON/API）
 */
@Data
@TableName("t_sys_permission")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
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
    private PermissionType type;

    /**
     * 対象パス（MENU/API 用）
     */
    private String path;

    /**
     * HTTPメソッド（API 用：GET/POST/PUT/DELETE/PATCH）
     * DBは varchar(10) のままでOK（enum名がそのまま保存される）
     */
    private HttpMethod method;

    /**
     * 表示順
     */
    private Integer sort;

    /**
     * 状態（0=無効、1=有効）
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
}
