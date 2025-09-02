package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageRequest {
    @Schema(description = "ページ番号（デフォルトは1）")
    private Long pageNum = 1L;   // 页码（默认1）
    @Schema(description = "1ページあたりの件数（デフォルトは10）")
    private Long pageSize = 10L; // 每页条数（默认10）

    public <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toPage() {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
    }
}
