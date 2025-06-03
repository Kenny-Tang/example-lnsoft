package com.redjujubetree.users.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
import com.redjujubetree.users.domain.entity.Article;

import java.util.Date;
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
		if (query.getSearchKey() != null && !query.getSearchKey().isEmpty()) {
			queryWrapper.and(wrapper -> wrapper
					.like(Article::getTitle, query.getSearchKey())
					.or()
					.like(Article::getSummary, query.getSearchKey()));
		}
		queryWrapper.orderByAsc(Article::getDisplayOrder).orderByDesc(Article::getUpdateTime);
		Page<Article> articlePage = selectPage(new Page<>(query.getPageNum(), query.getPageSize()), queryWrapper);
		return articlePage;
	}

	default boolean saveArticle(Article article) {
		if (article.getId() == null) {
			article.setId(com.redjujubetree.IdentifierGenerator.getDefaultGenerator().nextId());
		}
		Date updateTime = new Date();
		if (article.getCreateTime() == null) {
			article.setCreateTime(updateTime);
		}
		if (article.getUpdateTime() == null){
			article.setUpdateTime(updateTime);
		}
		if (article.getVersion() == null) {
			article.setVersion(0);
		}
		return insert(article) > 0;
	}


}
