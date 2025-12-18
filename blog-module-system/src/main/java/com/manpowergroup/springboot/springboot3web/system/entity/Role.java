package com.manpowergroup.springboot.springboot3web.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * ロールマスタ
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Getter
@Setter
@TableName("t_sys_role")
public class Role {

    /**
     * 主キーID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * ロールコード（例：ADMIN / USER）
     */
    private String code;

    /**
     * ロール名
     */
    private String name;

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
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    private LocalDateTime updatedAt;

    /**
     * 論理削除フラグ（0=未削除、1=削除済み）
     */
    private Byte isDeleted;
}
