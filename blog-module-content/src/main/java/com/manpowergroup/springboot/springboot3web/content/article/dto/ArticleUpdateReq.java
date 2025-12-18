package com.manpowergroup.springboot.springboot3web.content.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "ArticleUpdateReq", description = "記事更新リクエスト DTO")
public class ArticleUpdateReq {

    @NotNull
    @Schema(description = "記事ID", example = "10")
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Schema(description = "記事タイトル（最大200文字）", example = "更新後のタイトル")
    private String title;

    @Size(max = 512)
    @Schema(description = "記事概要（最大512文字、任意）", example = "更新後の概要")
    private String summary;

    @NotBlank
    @Schema(description = "記事本文")
    private String content;

    @NotNull
    @Schema(description = "カテゴリID（t_content_category.id）")
    private Long categoryId;

    @NotNull
    @Schema(description = "記事ステータス（0=下書き、1=公開、2=非公開）")
    private Byte status;
}
