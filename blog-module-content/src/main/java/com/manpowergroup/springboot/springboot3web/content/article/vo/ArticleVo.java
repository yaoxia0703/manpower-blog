package com.manpowergroup.springboot.springboot3web.content.article.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ArticleVo", description = "記事表示用VO")
public class ArticleVo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主キー")
    private Long id;

    @Schema(description = "記事タイトル")
    private String title;

    @Schema(description = "記事概要")
    private String summary;

    @Schema(description = "記事内容")
    private String content;

    @Schema(description = "作成者名")
    private String authorName;

    @Schema(description = "カテゴリ名")
    private String categoryName;

    @Schema(description = "作成日時")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private LocalDateTime updatedAt;

}

