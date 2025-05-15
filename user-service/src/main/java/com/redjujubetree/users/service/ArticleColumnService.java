package com.redjujubetree.users.service;

import com.redjujubetree.users.domain.entity.ArticleColumn;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章与专栏的关联关系 服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
public interface ArticleColumnService extends IService<ArticleColumn> {

    void saveArticleColumn(ArticleColumn articleColumn);

    void updateArticleColumnById(ArticleColumn articleColumn);
}
