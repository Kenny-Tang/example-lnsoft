package com.redjujubetree.users.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
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

	default Page<Article> getArticlePage(HomeViewArticleQueryDTO query) {
		LambdaQueryWrapper<Article> queryWrapper = Wrappers.lambdaQuery(Article.class);
		queryWrapper.ne(Article::getUrl, "/articles/Home.md");

		queryWrapper.orderByAsc(Article::getDisplayOrder);
		if (query.getSearchKey() != null && !query.getSearchKey().isEmpty()) {
			queryWrapper.and(wrapper -> wrapper
					.like(Article::getTitle, query.getSearchKey())
					.or()
					.like(Article::getSummary, query.getSearchKey()));
		}
		Page<Article> articlePage = selectPage(new Page<>(query.getPageNum(), query.getPageSize()), queryWrapper);
		return articlePage;
	}


}
