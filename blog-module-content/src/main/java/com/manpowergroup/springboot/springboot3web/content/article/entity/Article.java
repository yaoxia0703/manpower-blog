package com.manpowergroup.springboot.springboot3web.content.article.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 博客文章实体
 * </p>
 *
 * @author
 * @since 2025-08-24
 */
@Data
@TableName("t_content_article")
@Schema(name = "Article", description = "文章实体（t_content_article）")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "文章主键ID")
    private Long id;

    @Schema(description = "文章标题（最长200字符）")
    private String title;

    @Schema(description = "文章摘要（最长512字符，可为空）")
    private String summary;

    @Schema(description = "文章正文内容")
    private String content;

    @Schema(description = "分类ID（t_content_category.id）")
    private Long categoryId;

    @Schema(description = "作者ID（t_sys_user.id）")
    private Long authorId;

    @Schema(description = "文章状态（0=草稿，1=已发布，2=下线）")
    private Byte status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @TableLogic
    @Schema(description = "逻辑删除标志（0=未删除，1=已删除）")
    private Boolean isDeleted;
}
