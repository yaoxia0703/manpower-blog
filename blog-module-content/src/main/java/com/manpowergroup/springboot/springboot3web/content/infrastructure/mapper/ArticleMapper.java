package com.manpowergroup.springboot.springboot3web.content.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.springboot.springboot3web.content.domain.model.Article;
import com.manpowergroup.springboot.springboot3web.content.application.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.application.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 記事 Mapper インターフェース
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 記事一覧をページング形式で取得する
     *
     * @param request 検索条件
     * @param offset  オフセット
     * @param size    取得件数
     * @return 記事一覧
     */
    List<ArticleVo> selectPageVo(
            @Param("req") ArticleQueryRequest request,
            @Param("offset") Long offset,
            @Param("size") Long size
    );

    /**
     * 検索条件に一致する件数を取得する
     *
     * @param request 検索条件
     * @return 件数
     */
    Long countJoin(@Param("req") ArticleQueryRequest request);
}
