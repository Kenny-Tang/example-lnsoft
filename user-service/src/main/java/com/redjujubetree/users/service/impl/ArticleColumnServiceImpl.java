package com.redjujubetree.users.service.impl;

import com.redjujubetree.users.domain.entity.ArticleColumn;
import com.redjujubetree.users.mapper.ArticleColumnMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;
import java.util.Date;
import com.redjujubetree.users.service.ArticleColumnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章与专栏的关联关系 服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
@Service
public class ArticleColumnServiceImpl extends ServiceImpl<ArticleColumnMapper, ArticleColumn> implements ArticleColumnService {

    @Transactional(rollbackFor = Throwable.class)
	public void saveArticleColumn(ArticleColumn articleColumn) {

		ArticleColumn entity = new ArticleColumn();
		BeanUtil.copyProperties(articleColumn, entity);
		Date date = new Date();
		entity.setUpdateTime(date);
		entity.setCreateTime(date);
		entity.setVersion(0);
		save(entity);
	}

    public void updateArticleColumnById(ArticleColumn articleColumn) {
        articleColumn.setUpdateTime(new Date());
        updateById(articleColumn);
    }
}
