package com.redjujubetree.users.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
import com.redjujubetree.users.domain.entity.Article;
import com.redjujubetree.users.domain.entity.ColumnInfo;
import com.redjujubetree.users.mapper.ArticleMapper;
import com.redjujubetree.users.service.ArticleService;
import com.redjujubetree.users.service.ColumnInfoService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-13
 */
@Setter
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

	@Resource
	private ColumnInfoService columnInfoService;

    @Transactional(rollbackFor = Throwable.class)
	public void saveArticle(Article article) {

		Article entity = new Article();
		BeanUtil.copyProperties(article, entity);
		Date date = new Date();
		entity.setUpdateTime(date);
		entity.setCreateTime(date);
		entity.setVersion(0);
		save(entity);
	}

    public void updateArticleById(Article article) {
        article.setUpdateTime(new Date());
        updateById(article);
    }

	@Override
	public List<ArticleDTO> queryUserArticleList() {
		LambdaQueryWrapper<ColumnInfo> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.orderByAsc(ColumnInfo::getDisplayOrder);
		List<ColumnInfo> list = columnInfoService.list(queryWrapper);
		List<ArticleDTO> articleDTOS = getBaseMapper().queryArticleList();

		List<ArticleDTO> results = list.stream().map(e -> {
			ArticleDTO articleDTO = new ArticleDTO();
			BeanUtil.copyProperties(e, articleDTO);
			articleDTO.setId(e.getId().toString());
			articleDTO.setUpdateTime(e.getUpdateTime().toString());
			return articleDTO;
		}).collect(Collectors.toList());
		Map<String, ArticleDTO> columnMap = results.stream().collect(Collectors.toMap(ArticleDTO::getId, Function.identity()));

		for (ArticleDTO articleDTO : articleDTOS) {
			if (articleDTO.getColumnId() == null) {
				results.add(articleDTO);
			} else {
				ArticleDTO column = columnMap.get(articleDTO.getColumnId());
				if (column != null) {
					List<ArticleDTO> children = column.getChildren();
					if (children == null) {
						children = new ArrayList<>();
						column.setChildren(children);
					}
					children.add(articleDTO);
				}
			}
		}
		Collections.sort(results, Comparator.comparing(ArticleDTO::getDisplayOrder));
		return results;
	}

	public Page<Article> queryHomeView(HomeViewArticleQueryDTO query) {
		LambdaQueryWrapper<Article> queryWrapper = Wrappers.lambdaQuery(Article.class);
		queryWrapper.ne(Article::getUrl, "/articles/Home.md");
		queryWrapper.orderByAsc(Article::getDisplayOrder);
		Page<Article> articlePage = getBaseMapper().selectPage(new Page<>(query.getPageNum(), query.getPageSize()), queryWrapper);

		return  articlePage;
	}
}
