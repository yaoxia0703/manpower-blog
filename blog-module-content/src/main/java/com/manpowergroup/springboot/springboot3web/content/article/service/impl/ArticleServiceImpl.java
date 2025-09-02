package com.manpowergroup.springboot.springboot3web.content.article.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.article.entity.Article;
import com.manpowergroup.springboot.springboot3web.content.article.mapper.ArticleMapper;
import com.manpowergroup.springboot.springboot3web.content.article.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.springboot.springboot3web.content.article.vo.ArticleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.manpowergroup.springboot.springboot3web.blog.common.util.ServiceHelper.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-08-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    @Transactional(readOnly = true)
    public JoinPageResult<ArticleVo> queryArticlePageVo(ArticleQueryRequest request) {
        // 1. 分页参数兜底
        var p = safePageNum(request.getPageNum());
        var s = safePageSize(request.getPageSize());
        var offset = (p - 1) * s;

        // 2. 查询数据和总数
        var records = this.baseMapper.selectPageVo(request, offset, s);
        var total = this.baseMapper.countJoin(request);

        // 3. 返回结果
        return JoinPageResult.of(records, total, p, s);
    }
}

