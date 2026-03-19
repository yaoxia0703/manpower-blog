package com.manpowergroup.springboot.springboot3web.content.domain.repository;

import com.manpowergroup.springboot.springboot3web.content.domain.model.Article;
import com.manpowergroup.springboot.springboot3web.content.application.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.application.vo.ArticleVo;

import java.util.List;

public interface ArticleRepository {

    List<ArticleVo> selectPageVo(ArticleQueryRequest request, Long offset, Long size);

    Long countJoin(ArticleQueryRequest request);

    Article findById(Long id);

    boolean save(Article article);

    boolean updateById(Article article);

    boolean removeById(Long id);
}
