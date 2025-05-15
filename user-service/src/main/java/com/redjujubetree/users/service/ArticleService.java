package com.redjujubetree.users.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redjujubetree.users.domain.dto.ArticleDTO;
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

    void updateArticleById(Article article);

    List<ArticleDTO> queryUserArticleList();
}
