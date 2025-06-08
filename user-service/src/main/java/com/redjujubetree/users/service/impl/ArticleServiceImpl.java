package com.redjujubetree.users.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.KStringUtils;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
import com.redjujubetree.users.domain.entity.Article;
import com.redjujubetree.users.domain.entity.ArticleColumn;
import com.redjujubetree.users.domain.entity.ColumnInfo;
import com.redjujubetree.users.mapper.ArticleColumnMapper;
import com.redjujubetree.users.mapper.ArticleMapper;
import com.redjujubetree.users.mapper.ColumnInfoMapper;
import com.redjujubetree.users.service.ArticleService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
	private ColumnInfoMapper columnInfoMapper;
	@Resource
	private ArticleColumnMapper articleColumnMapper;

    @Transactional(rollbackFor = Throwable.class)
	public void saveArticle(Article article) {
		baseMapper.saveArticle(article);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveArticle(String articleFile, ArticleDTO article) {
		Article article1 = new Article();
		BeanUtils.copyProperties(article, article1);
		String kebabCase = KStringUtils.toKebabCase(articleFile.replace(" ", ""));
		if ("/articles/".equals(article.getUrl())) {
			article1.setUrl(article.getUrl()+ articleFile);
		}
		if ("/articles/".equals(article.getPath())) {
			article1.setPath(article.getPath() + kebabCase.replace(".md", ""));
		}
		saveArticle(article1);
		if (Objects.nonNull(article.getColumnId())) {
			ArticleColumn entity = new ArticleColumn();
			entity.setArticleId(article1.getId());
			entity.setColumnId(Long.valueOf(article.getColumnId()));
			articleColumnMapper.saveArticleColumn(entity);
		}
	}

	public void updateArticleById(Article article) {
        article.setUpdateTime(new Date());
        updateById(article);
    }

	@Override
	public List<ArticleDTO> queryUserArticleList() {
		List<ColumnInfo> list = columnInfoMapper.getColumnInfosWithDisplayOrder();
		List<ArticleDTO> articleDTOS = baseMapper.queryArticleList();

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
		Page<Article> articlePage = baseMapper.getArticlePage(query);
		return  articlePage;
	}


}
