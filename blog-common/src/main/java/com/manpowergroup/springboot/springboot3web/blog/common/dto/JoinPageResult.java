package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 複数テーブルを結合した検索結果用ページオブジェクト
 * </p>
 * <p>
 * 通常の PageResult と区別するために作成。
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "JoinPageResult", description = "複数テーブル結合検索結果用ページオブジェクト")
public class JoinPageResult<T> implements Serializable {

    @Schema(description = "データリスト")
    private List<T> records;

    @Schema(description = "総件数")
    private long total;

    @Schema(description = "現在のページ番号")
    private long pageNum;

    @Schema(description = "1ページあたりの件数")
    private long pageSize;

    @Schema(description = "総ページ数")
    private long pages;

    public JoinPageResult(List<T> records, long total, long pageNum, long pageSize) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = (pageSize == 0) ? 0 : (long) Math.ceil((double) total / pageSize);
    }

    public static <T> JoinPageResult<T> of(List<T> records, long total, long pageNum, long pageSize) {
        long safeSize = (pageSize <= 0) ? 10 : Math.min(pageSize, 100);
        long pages = (long) Math.ceil((double) total / safeSize);
        return new JoinPageResult<>(records, total, pageNum, safeSize, pages);
    }

}

