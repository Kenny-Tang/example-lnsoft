package com.redjujubetree.users.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.redjujubetree.users.model.dto.ArticleDTO;
import com.redjujubetree.users.model.entity.Article;
import com.redjujubetree.users.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ResponseBody
    @RequestMapping("/list" )
    public List<ArticleDTO> hello() {
        List<Article> list = articleService.list();
        List<ArticleDTO> articleDTOList = Collections.emptyList();
        if (CollectionUtil.isNotEmpty(list)) {
            articleDTOList = list.stream().map(t -> {
                ArticleDTO articleDTO = new ArticleDTO();
                articleDTO.setId(t.getId().toString());
                articleDTO.setPath(t.getPath());
                articleDTO.setName(t.getName());
                articleDTO.setUrl(t.getUrl());
                articleDTO.setIcon(t.getIcon());
                articleDTO.setComponent(t.getComponent());
                articleDTO.setCreateTime(t.getCreateTime().toString());
                return articleDTO;
            }).collect(Collectors.toList());
        }
        return articleDTOList;
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
