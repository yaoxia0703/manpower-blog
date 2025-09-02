package com.manpowergroup.springboot.springboot3web.portal.article;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.article.entity.Article;
import com.manpowergroup.springboot.springboot3web.content.article.ArticleService;
import com.manpowergroup.springboot.springboot3web.content.article.vo.ArticleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleCreateController {
    private final ArticleService articleService;

    @PostMapping
    public Result<Long> create(@RequestBody CreateReq req) {
        Article a = new Article();
        a.setTitle(req.getTitle());
        a.setContent(req.getContent());
        a.setStatus(req.getStatus());
        // 若你已有 MetaObjectHandler，可删掉下面两行
        a.setCreatedAt(LocalDateTime.now());
        a.setUpdatedAt(LocalDateTime.now());

        boolean ok = articleService.save(a);
        return ok ? Result.ok(a.getId()) : Result.error(500, "create failed");
    }

    @Data
    public static class CreateReq {
        private String title;
        private String content;
        private byte status; // 0-草稿 1-发布（默认1）
    }


    @GetMapping("pageList")
    @Operation(summary = "ページング処理", description = "初期化ページングと簡単な条件にとってページング処理を行う")
    public Result<JoinPageResult<ArticleVo>> pageList(@RequestBody ArticleQueryRequest request) {
        JoinPageResult<ArticleVo> joinPageResult = articleService.queryArticlePageVo(request);
        return Result.ok(joinPageResult);
    }

}
