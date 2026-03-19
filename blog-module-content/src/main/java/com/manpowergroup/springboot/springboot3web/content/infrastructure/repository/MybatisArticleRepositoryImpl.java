package com.manpowergroup.springboot.springboot3web.content.infrastructure.repository;

import com.manpowergroup.springboot.springboot3web.content.domain.model.Article;
import com.manpowergroup.springboot.springboot3web.content.domain.repository.ArticleRepository;
import com.manpowergroup.springboot.springboot3web.content.application.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.infrastructure.mapper.ArticleMapper;
import com.manpowergroup.springboot.springboot3web.content.application.vo.ArticleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MybatisArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMapper articleMapper;


    public MybatisArticleRepositoryImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public List<ArticleVo> selectPageVo(ArticleQueryRequest request, Long offset, Long size) {
        return articleMapper.selectPageVo(request, offset, size);
    }

    @Override
    public Long countJoin(ArticleQueryRequest request) {
        return articleMapper.countJoin(request);
    }

    @Override
    public Article findById(Long id) {
        return articleMapper.selectById(id);
    }

    @Override
    public boolean save(Article article) {
        return articleMapper.insert(article) > 0;
    }

    @Override
    public boolean updateById(Article article) {
        return articleMapper.updateById(article) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return articleMapper.deleteById(id) > 0;
    }
}
