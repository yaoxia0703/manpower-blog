package com.manpowergroup.springboot.springboot3web.content.article.service;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.article.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.springboot.springboot3web.content.article.vo.ArticleVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-08-24
 */
public interface ArticleService extends IService<Article> {

    JoinPageResult<ArticleVo> queryArticlePageVo(ArticleQueryRequest request);

    ArticleVo getArticleVoById(Long id);
}
