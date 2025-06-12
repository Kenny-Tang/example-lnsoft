package com.redjujubetree.users.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

	Page<ArticleDTO> queryArticlePage(Page<ArticleDTO> page, HomeViewArticleQueryDTO query) ;

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
