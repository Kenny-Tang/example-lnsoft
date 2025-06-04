package com.redjujubetree.users.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redjujubetree.users.domain.entity.ArticleColumn;

/**
 * <p>
 * 文章与专栏的关联关系 Mapper 接口
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
public interface ArticleColumnMapper extends BaseMapper<ArticleColumn> {

	default boolean saveArticleColumn(ArticleColumn en){
		if (en.getId() == null) {
			en.setId(com.redjujubetree.IdentifierGenerator.getDefaultGenerator().nextId());
		}
		if (en.getVersion() == null) {
			en.setVersion(0);
		}
		if (en.getCreateTime() == null) {
			en.setCreateTime(new java.util.Date());
		}
		if (en.getUpdateTime() == null) {
			en.setUpdateTime(new java.util.Date());
		}
		return insert(en) > 0;
	}
}
