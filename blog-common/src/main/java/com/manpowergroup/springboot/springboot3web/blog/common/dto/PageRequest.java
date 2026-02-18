package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageRequest {

    @Schema(description = "ページ番号")
    private Long pageNum;

    @Schema(description = "1ページあたりの件数")
    private Long pageSize;
}
