package com.manpowergroup.springboot.springboot3web.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@TableName("t_sys_role")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Role {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;

    private String name;

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
