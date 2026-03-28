package com.manpowergroup.springboot.springboot3web.system.domain.model.menu;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.MenuType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
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




    /**
     * メニュー種別に応じたバリデーション
     * ・ディレクトリ：name 必須、path 不要
     * ・メニュー：path 必須
     * ・ボタン：path 不要
     */
    public void validateByType() {

        switch (this.type) {
            case DIRECTORY -> {
                if (!StringUtils.hasText(this.name)) {
                    throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ディレクトリ名は必須です");
                }
            }
            case MENU -> {
                if (!StringUtils.hasText(this.path)) {
                    throw BizException.withDetail(ErrorCode.BAD_REQUEST, "メニューにはpathが必要です");
                }
            }
            case BUTTON -> {
                if (StringUtils.hasText(this.path)) {
                    throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ボタンにはpathを設定できません");
                }
            }
            default -> throw BizException.withDetail(ErrorCode.BAD_REQUEST, "不正なメニュー種別です");
        }
    }

    /**
     * 同一階層の名称重複チェック
     *
     * @param exists 同一階層に同名メニューが存在する場合 true
     */
    public void validateDuplicateName(boolean exists) {
        if (exists) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "同一階層に同名メニューが存在します");
        }
    }

    /**
     * pathの一意性チェック（全体で一意）
     *
     * @param exists 同一pathのメニューが存在する場合 true
     */
    public void validateDuplicatePath(boolean exists) {
        if (exists) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "path が既に存在します");
        }
    }

    /**
     * 自身を親に設定できないことを検証する
     *
     * @param id 自身のID
     */
    public void validateNotSelfParent(Long id) {
        if (parentId.equals(id)) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "自身を親に設定できません");
        }
    }

    /**
     * タイプ変更の制約チェック
     *
     * @param oldType 変更前のタイプ
     * @param newType 変更後のタイプ
     * @param hasChildren 子メニューが存在する場合 true
     */
    public void validateTypeChange(MenuType oldType, MenuType newType, boolean hasChildren) {
        if (oldType != newType && hasChildren) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "子メニューが存在するためタイプ変更できません");
        }
    }

    /**
     * 削除可否チェック
     *
     * @param hasChildren 子メニューが存在する場合 true
     * @param isUsed 他機能で使用されている場合 true
     */
    public void validateDeletable(boolean hasChildren, boolean isUsed) {

        if (hasChildren) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "子メニューが存在するため削除できません");
        }

        if (isUsed) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "該当メニューは使用中のため削除できません");
        }
    }

    /**
     * ステータス変更
     *
     * @param newStatus 変更後ステータス
     */
    public void changeStatus(Status newStatus) {
        if (this.status == newStatus) {
            return;
        }

        this.status = newStatus;
    }
}
