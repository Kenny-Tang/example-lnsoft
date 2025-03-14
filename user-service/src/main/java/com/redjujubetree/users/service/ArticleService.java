package com.redjujubetree.users.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.users.mapper.ArticleMapper;
import com.redjujubetree.users.model.entity.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

	public List<Article> list() {
		LambdaQueryWrapper<Article> queryWrapper = Wrappers.lambdaQuery();
		return baseMapper.selectList(queryWrapper);
	}
}
