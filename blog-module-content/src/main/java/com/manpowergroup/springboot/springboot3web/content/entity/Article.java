package com.manpowergroup.springboot.springboot3web.content.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 記事エンティティ（t_content_article）
 */
@Data
@TableName("t_content_article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 記事ID（主キー）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 記事タイトル（最大200文字）
     */
    private String title;

    /**
     * 記事概要（最大512文字、任意）
     */
    private String summary;

    /**
     * 記事本文
     */
    private String content;

    /**
     * カテゴリID（t_content_category.id）
     */
    private Long categoryId;

    /**
     * 作成者ID（t_sys_user.id）
     */
    private Long authorId;

    /**
     * 記事ステータス（0=下書き、1=公開、2=非公開）
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
