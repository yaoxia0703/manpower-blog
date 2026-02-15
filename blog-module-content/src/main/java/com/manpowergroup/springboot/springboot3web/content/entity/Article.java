package com.manpowergroup.springboot.springboot3web.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 記事エンティティ
 */
@Data
@TableName("t_content_article")
@Schema(name = "Article", description = "記事エンティティ（t_content_article）")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "記事ID（主キー）")
    private Long id;

    @Schema(description = "記事タイトル（最大200文字）")
    private String title;

    @Schema(description = "記事概要（最大512文字、任意）")
    private String summary;

    @Schema(description = "記事本文")
    private String content;

    @Schema(description = "カテゴリID（t_content_category.id）")
    private Long categoryId;

    @Schema(description = "作成者ID（t_sys_user.id）")
    private Long authorId;

    @Schema(description = "記事ステータス（0=下書き、1=公開、2=非公開）")
    private Byte status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "作成日時")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新日時")
    private LocalDateTime updatedAt;

    @TableLogic
    @Schema(description = "論理削除フラグ（0=未削除、1=削除済み）")
    private Boolean isDeleted;
}
