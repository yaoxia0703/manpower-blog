package com.manpowergroup.springboot.springboot3web.content.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "ArticleCreateReq", description = "記事作成リクエスト DTO")
public class ArticleCreateReq {

    @NotBlank
    @Size(max = 200)
    @Schema(description = "記事タイトル（最大200文字）", example = "初めてのブログ記事")
    private String title;

    @Size(max = 512)
    @Schema(description = "記事概要（最大512文字、任意）", example = "この記事の概要です")
    private String summary;

    @NotBlank
    @Schema(description = "記事本文", example = "本文内容…")
    private String content;

    @NotNull
    @Schema(description = "カテゴリID（t_content_category.id）", example = "1")
    private Long categoryId;

    @NotNull
    @Schema(description = "作成者ID（t_sys_user.id）", example = "1001")
    private Long authorId;

    @NotNull
    @Schema(description = "記事ステータス（0=下書き、1=公開、2=非公開）", example = "1")
    private Byte status;
}
