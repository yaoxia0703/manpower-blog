package com.manpowergroup.springboot.springboot3web.system.domain.model.menu;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.MenuType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * システムメニュー管理テーブル
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_sys_menu")
public class Menu {

    /**
     * 主キーID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 親メニューID（0は最上位）
     */
    private Long parentId;

    /**
     * メニュー名称
     */
    private String name;

    /**
     * フロントエンドのルートパス（type=2の場合は設定推奨）
     */
    private String path;

    /**
     * フロントエンドのコンポーネントパス
     */
    private String component;

    /**
     * 権限識別子（例：sys:user:list、ディレクトリはNULL可）
     */
    private String permission;

    /**
     * メニュー種別（1=ディレクトリ 2=メニュー 3=ボタン）
     */
    private MenuType type;

    /**
     * 表示順
     */
    private Integer sort;

    /**
     * アイコン
     */
    private String icon;

    /**
     * 状態（0=無効 1=有効）
     */
    private Status status;

    /**
     * 論理削除フラグ（0=未削除 1=削除済）
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Byte isDeleted;

    /**
     * 作成日時
     */
    @TableField(fill = FieldFill.INSERT) // レコード挿入時に自動で値をセット
    private LocalDateTime createTime;

    /**
     * 更新日時
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
