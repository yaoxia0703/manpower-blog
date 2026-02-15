package com.manpowergroup.springboot.springboot3web.content.dto;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "記事タイトル（曖昧検索可能）")
    private String title;

    //private String content;

    @Schema(description = "記事状態：0＝下書き、1＝公開、2＝非公開")
    private Integer status;

    @Schema(description = "カテゴリID")
    private Long categoryId;

    //private String summary;


}
