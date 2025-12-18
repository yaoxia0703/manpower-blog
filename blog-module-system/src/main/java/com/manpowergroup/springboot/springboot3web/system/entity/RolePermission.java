package com.manpowergroup.springboot.springboot3web.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * ロール・権限紐付け
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Getter
@Setter
@TableName("t_sys_role_permission")
public class RolePermission {

    /**
     * 主キーID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * ロールID（t_sys_role.id）
     */
    private Long roleId;

    /**
     * 権限ID（t_sys_permission.id）
     */
    private Long permissionId;

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
