package com.manpowergroup.springboot.springboot3web.system.domain.model.role;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * ロールメニュー関連テーブル
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_sys_role_menu")
public class RoleMenu {

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
     * メニューID（t_sys_menu.id）
     */
    private Long menuId;

    /**
     * 作成日時
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
