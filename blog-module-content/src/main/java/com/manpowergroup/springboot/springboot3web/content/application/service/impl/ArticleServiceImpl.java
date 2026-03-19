package com.manpowergroup.springboot.springboot3web.content.application.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.content.domain.model.Article;
import com.manpowergroup.springboot.springboot3web.content.domain.repository.ArticleRepository;
import com.manpowergroup.springboot.springboot3web.content.application.dto.ArticleCreateReq;
import com.manpowergroup.springboot.springboot3web.content.application.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.application.dto.ArticleUpdateReq;
import com.manpowergroup.springboot.springboot3web.content.application.service.ArticleService;
import com.manpowergroup.springboot.springboot3web.content.application.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.manpowergroup.springboot.springboot3web.blog.common.util.ServiceHelper.safePageNum;
import static com.manpowergroup.springboot.springboot3web.blog.common.util.ServiceHelper.safePageSize;

/**
 * 記事サービス実装クラス
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public JoinPageResult<ArticleVo> queryArticlePageVo(ArticleQueryRequest request) {

        var p = safePageNum(request.getPageNum());
        var s = safePageSize(request.getPageSize());
        var offset = (p - 1) * s;

        var records = articleRepository.selectPageVo(request, offset, s);
        var total = articleRepository.countJoin(request);

        return JoinPageResult.of(records, total, p, s);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleVo getArticleVoById(Long id) {

        var article = Optional.ofNullable(id)
                .map(articleRepository::findById)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

        ArticleVo vo = new ArticleVo();
        BeanUtils.copyProperties(article, vo);
        return vo;
    }

    @Override
    @Transactional
    public Long addArticle(ArticleCreateReq req) {

        var safeReq = Optional.ofNullable(req)
                .orElseThrow(() -> new BizException(ErrorCode.BAD_REQUEST));

        Article article = new Article();
        BeanUtils.copyProperties(safeReq, article);

        boolean saved = articleRepository.save(article);
        if (!saved) {
            throw new BizException(ErrorCode.SERVER_ERROR);
        }
        return article.getId();
    }

    @Override
    @Transactional
    public Boolean updateArticle(ArticleUpdateReq req) {

        var safeReq = Optional.ofNullable(req)
                .orElseThrow(() -> new BizException(ErrorCode.BAD_REQUEST));

        Article article = new Article();
        BeanUtils.copyProperties(safeReq, article);

        boolean updated = articleRepository.updateById(article);
        if (!updated) {
            throw new BizException(ErrorCode.SERVER_ERROR);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteArticle(Long id) {

        Optional.ofNullable(id)
                .orElseThrow(() -> new BizException(ErrorCode.BAD_REQUEST));

        boolean deleted = articleRepository.removeById(id);
        if (!deleted) {
            throw new BizException(ErrorCode.SERVER_ERROR);
        }
        return true;
    }
}
