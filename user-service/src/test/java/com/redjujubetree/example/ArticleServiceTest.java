package com.redjujubetree.example;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redjujubetree.IdentifierGenerator;
import com.redjujubetree.KStringUtils;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
import com.redjujubetree.users.domain.entity.Article;
import com.redjujubetree.users.domain.entity.ArticleColumn;
import com.redjujubetree.users.domain.entity.ColumnInfo;
import com.redjujubetree.users.service.ArticleColumnService;
import com.redjujubetree.users.service.ArticleService;
import com.redjujubetree.users.service.ColumnInfoService;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Setter
@SpringBootTest
public class ArticleServiceTest {

    @Resource
    private ArticleService articleService;
    @Resource
    private ColumnInfoService columnInfoService;
    @Resource
    private ArticleColumnService articleColumnService;

    @Test
    public void testSaveArticle() {
        // 测试保存文章
        String mdArticle = "MyBatisPlusPage.md";
        System.out.println(mdArticle);
        Article articleDTO = new Article();
        articleDTO.setTitle("MyBatisPlus 分页查询");
        articleDTO.setPath("/articles/"+ KStringUtils.toKebabCase(mdArticle.replace(".md", "")));
        articleDTO.setUrl("/articles/" + mdArticle);
        articleDTO.setIcon("Document");
        articleDTO.setComponent("MdViewer");
        articleDTO.setSummary("在使用 MyBatis-Plus 时，分页功能是其非常常用的一个特性，配置简单，使用方便。以下是 MyBatis-Plus 分页的完整使用教程，适用于 Spring Boot 项目。");
        articleService.saveArticle(articleDTO);
        System.out.println(JSON.toJSONString(articleDTO));
    }

    @Test
    public void testSelectArticle() {
        // 测试查询文章
        Article article = articleService.getById(710879483208421378L);
        System.out.println(JSON.toJSONString(article));
    }
    @Test
    public void testListArticles() {
        // 测试列表文章
        List<ArticleDTO> articleDTOS = articleService.queryUserArticleList();
        System.out.println(JSON.toJSONString(articleDTOS));
    }

    @Test
    public void testHomeView() {
        // 测试首页文章
        HomeViewArticleQueryDTO query = new HomeViewArticleQueryDTO();
        Page<ArticleDTO> articleDTOS = articleService.queryHomeView(query);
        System.out.println(JSON.toJSONString(articleDTOS));
        query.setPageNum(2);
        articleDTOS = articleService.queryHomeView(query);
        System.out.println(JSON.toJSONString(articleDTOS));
    }

    private void saveArticleColumn(Article article, ColumnInfo columnInfo) {
        LambdaQueryWrapper<ArticleColumn> query = Wrappers.lambdaQuery();
        query.eq(ArticleColumn::getArticleId, article.getId()).eq(ArticleColumn::getColumnId, columnInfo.getId());
        ArticleColumn one = articleColumnService.getOne(query);
        if (one == null) {
            ArticleColumn entity1 = new ArticleColumn();
            entity1.setArticleId(article.getId());
            entity1.setColumnId(columnInfo.getId());
            entity1.setId(IdentifierGenerator.getDefaultGenerator().nextId());
            articleColumnService.saveArticleColumn(entity1);
        }
    }

    private ColumnInfo saveColumn(ArticleDTO articleDTO) {
        LambdaQueryWrapper<ColumnInfo> query = Wrappers.lambdaQuery();
        query.eq(ColumnInfo::getPath, articleDTO.getPath());
        ColumnInfo one = columnInfoService.getOne(query);
        if (one != null) {
            return one;
        }
        ColumnInfo entity = new ColumnInfo();
        BeanUtil.copyProperties(articleDTO, entity);
        entity.setId(IdentifierGenerator.getDefaultGenerator().nextId());
        columnInfoService.saveColumnInfo(entity);
        return entity;
    }

    private Article saveArticle(ArticleDTO articleDTO) {
        LambdaQueryWrapper<Article> query = Wrappers.lambdaQuery();
        query.eq(Article::getPath, articleDTO.getPath());
        Article one = articleService.getOne(query);
        if (one == null) {
            Article entity = new Article();
            BeanUtil.copyProperties(articleDTO, entity);
            entity.setId(IdentifierGenerator.getDefaultGenerator().nextId());
            articleService.saveArticle(entity);
            return entity;
        }
        return one;
    }
}
