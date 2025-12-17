package com.manpowergroup.springboot.springboot3web.content.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "ArticleUpdateReq", description = "文章编辑请求 DTO")
public class ArticleUpdateReq {

    @NotNull
    @Schema(description = "文章ID", example = "10")
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Schema(description = "文章标题（最长200字符）", example = "修改后的标题")
    private String title;

    @Size(max = 512)
    @Schema(description = "文章摘要（最长512字符，可为空）", example = "修改后的摘要")
    private String summary;

    @NotBlank
    @Schema(description = "文章正文内容")
    private String content;

    @NotNull
    @Schema(description = "分类ID（t_content_category.id）")
    private Long categoryId;

    @NotNull
    @Schema(description = "文章状态（0=草稿，1=已发布，2=下线）")
    private Byte status;
}
