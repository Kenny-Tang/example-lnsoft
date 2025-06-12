package com.redjujubetree.users.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redjujubetree.users.domain.bo.ArticleBO;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
import com.redjujubetree.users.domain.entity.Article;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-13
 */
public interface ArticleService extends IService<Article> {

    void saveArticle(Article article);

    void updateArticleById(ArticleDTO article);

    List<ArticleDTO> queryUserArticleList();

    Page<ArticleDTO> queryHomeView(HomeViewArticleQueryDTO query);

	void saveArticle(String originalFilename, ArticleDTO article);

	ArticleBO getArticleById(Long id) ;

}
