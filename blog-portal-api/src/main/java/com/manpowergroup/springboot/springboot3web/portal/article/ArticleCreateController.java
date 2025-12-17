package com.manpowergroup.springboot.springboot3web.portal.article;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleCreateReq;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.article.entity.Article;
import com.manpowergroup.springboot.springboot3web.content.article.service.ArticleService;
import com.manpowergroup.springboot.springboot3web.content.article.vo.ArticleVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleCreateController {
    private final ArticleService articleService;

    @PostMapping("/add")
    public Result<Long> add(
            @Valid @RequestBody ArticleCreateReq req
    ) {
        Long id = articleService.addArticle(req);
        return Result.ok(id);
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
