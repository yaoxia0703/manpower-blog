package com.manpowergroup.springboot.springboot3web.content.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleCreateReq;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleUpdateReq;
import com.manpowergroup.springboot.springboot3web.content.article.entity.Article;
import com.manpowergroup.springboot.springboot3web.content.article.vo.ArticleVo;

/**
 * 記事サービスインターフェース
 */
public interface ArticleService extends IService<Article> {

    /**
     * 記事一覧をページング形式で取得する
     *
     * @param request 検索条件
     * @return ページング結果
     */
    JoinPageResult<ArticleVo> queryArticlePageVo(ArticleQueryRequest request);

    /**
     * 記事IDを指定して記事情報を取得する
     *
     * @param id 記事ID
     * @return 記事情報
     */
    ArticleVo getArticleVoById(Long id);

    /**
     * 記事を新規作成する
     *
     * @param req 記事作成リクエスト
     * @return 作成された記事ID
     */
    Long addArticle(ArticleCreateReq req);

    /**
     * 記事を更新する
     *
     * @param req 記事更新リクエスト
     * @return 更新結果
     */
    Boolean updateArticle(ArticleUpdateReq req);

    /**
     * 記事を削除する（論理削除）
     *
     * @param id 記事ID
     * @return 削除結果
     */
    Boolean deleteArticle(Long id);
}
