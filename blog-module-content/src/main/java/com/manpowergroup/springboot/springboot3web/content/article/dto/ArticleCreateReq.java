package com.manpowergroup.springboot.springboot3web.content.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "ArticleCreateReq", description = "文章新增请求 DTO")
public class ArticleCreateReq {

    @NotBlank
    @Size(max = 200)
    @Schema(description = "文章标题（最长200字符）", example = "我的第一篇博客")
    private String title;

    @Size(max = 512)
    @Schema(description = "文章摘要（最长512字符，可为空）", example = "这是文章摘要")
    private String summary;

    @NotBlank
    @Schema(description = "文章正文内容", example = "正文内容……")
    private String content;

    @NotNull
    @Schema(description = "分类ID（t_content_category.id）", example = "1")
    private Long categoryId;

    @NotNull
    @Schema(description = "作者ID（t_sys_user.id）", example = "1001")
    private Long authorId;

    @NotNull
    @Schema(description = "文章状态（0=草稿，1=已发布，2=下线）", example = "1")
    private Byte status;
}
