package com.manpowergroup.springboot.springboot3web.portal.article;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleCreateReq;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleUpdateReq;
import com.manpowergroup.springboot.springboot3web.content.article.service.ArticleService;
import com.manpowergroup.springboot.springboot3web.content.article.vo.ArticleVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleCreateController {

    private final ArticleService articleService;

    @Operation(
            summary = "記事の新規作成",
            description = "記事情報を新規登録し、作成された記事IDを返却する"
    )
    @PostMapping("add")
    public Result<Long> add(
            @Valid @RequestBody ArticleCreateReq req
    ) {
        return Result.ok(articleService.addArticle(req));
    }

    @Operation(
            summary = "記事一覧取得（ページング）",
            description = "指定された条件に基づき、記事一覧をページング形式で取得する"
    )
    @GetMapping("pageList")
    public Result<JoinPageResult<ArticleVo>> pageList(
            @RequestBody ArticleQueryRequest request
    ) {
        JoinPageResult<ArticleVo> joinPageResult =
                articleService.queryArticlePageVo(request);
        return Result.ok(joinPageResult);
    }

    @Operation(
            summary = "記事の更新",
            description = "指定された記事IDの情報を更新する"
    )
    @PutMapping("update")
    public Result<Boolean> update(
            @Valid @RequestBody ArticleUpdateReq req
    ) {
        return Result.ok(articleService.updateArticle(req));
    }

    @Operation(
            summary = "記事の論理削除",
            description = "指定された記事IDを論理削除する"
    )
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(
            @PathVariable Long id
    ) {
        return Result.ok(articleService.deleteArticle(id));
    }
}
