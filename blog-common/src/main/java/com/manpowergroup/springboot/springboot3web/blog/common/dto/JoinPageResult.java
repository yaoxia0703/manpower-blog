package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 複数テーブル結合検索結果用ページオブジェクト
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "JoinPageResult", description = "複数テーブル結合検索結果用ページオブジェクト")
public class JoinPageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * pages はここだけで計算する（ロジックの一元化）
     */
    public JoinPageResult(List<T> records, long total, long pageNum, long pageSize) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = calculatePages(total, pageSize);
    }

    /**
     * factory method（ページ情報の保持のみ。補正は PageUtil 側で行う）
     */
    public static <T> JoinPageResult<T> of(List<T> records, long total, long pageNum, long pageSize) {
        return new JoinPageResult<>(records, total, pageNum, pageSize);
    }

    private static long calculatePages(long total, long pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        return (long) Math.ceil((double) total / pageSize);
    }
}
