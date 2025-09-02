package com.manpowergroup.springboot.springboot3web.content.article.mapper;

import com.manpowergroup.springboot.springboot3web.content.article.dto.ArticleQueryRequest;
import com.manpowergroup.springboot.springboot3web.content.article.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.springboot.springboot3web.content.article.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-08-24
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<ArticleVo> selectPageVo(@Param("req") ArticleQueryRequest request,@Param("offset") Long offset, @Param("size") Long size);

    Long countJoin(@Param("req") ArticleQueryRequest request);

}
