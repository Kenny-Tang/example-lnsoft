package com.redjujubetree.users.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.entity.Article;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tanjianwei
 * @since 2025-02-27
 */
public interface ArticleMapper extends BaseMapper<Article> {

	List<ArticleDTO> queryArticleList() ;
}
