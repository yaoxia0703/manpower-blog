package com.manpowergroup.springboot.springboot3web.content.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleCreateReq;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleUpdateReq;
import com.manpowergroup.springboot.springboot3web.content.article.entity.Article;
import com.manpowergroup.springboot.springboot3web.content.article.mapper.ArticleMapper;
import com.manpowergroup.springboot.springboot3web.content.article.service.ArticleService;
import com.manpowergroup.springboot.springboot3web.content.article.vo.ArticleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.manpowergroup.springboot.springboot3web.blog.common.util.ServiceHelper.*;

/**
 * 記事サービス実装クラス
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Override
    @Transactional(readOnly = true)
    public JoinPageResult<ArticleVo> queryArticlePageVo(ArticleQueryRequest request) {

        // ページングパラメータの補正
        var p = safePageNum(request.getPageNum());
        var s = safePageSize(request.getPageSize());
        var offset = (p - 1) * s;

        // 一覧データおよび総件数の取得
        var records = this.baseMapper.selectPageVo(request, offset, s);
        var total = this.baseMapper.countJoin(request);

        // ページング結果を返却
        return JoinPageResult.of(records, total, p, s);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleVo getArticleVoById(Long id) {

        var article = Optional.ofNullable(id)
                .map(this.baseMapper::selectById)
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

        boolean saved = this.save(article);
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

        boolean updated = this.updateById(article);
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

        boolean deleted = this.removeById(id);
        if (!deleted) {
            throw new BizException(ErrorCode.SERVER_ERROR);
        }
        return true;
    }
}
