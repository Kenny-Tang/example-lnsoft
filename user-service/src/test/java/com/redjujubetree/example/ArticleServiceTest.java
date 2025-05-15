package com.redjujubetree.example;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.redjujubetree.IdentifierGenerator;
import com.redjujubetree.users.domain.dto.ArticleDTO;
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
    public void testListArticles() {
        // 测试列表文章
        List<ArticleDTO> articleDTOS = articleService.queryUserArticleList();
        System.out.println(JSON.toJSONString(articleDTOS));
    }

    @Test
    public void test() {
        String str = "[\n" +
                "\t{ \n" +
                "\t\t\"path\": \"/\",\n" +
                "\t\t\"title\": \"首页\", \n" +
                "\t\t\"url\": \"/articles/Home.md\",\n" +
                "\t\t\"component\": \"HomeView\" ,\n" +
                "\t\t\"icon\": \"House\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"发票获取方案设计\",\n" +
                "\t\t\"path\": \"/articles/invoice-fetch-scheme\",\n" +
                "\t\t\"url\": \"/articles/发票获取方案设计.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"Linux reset password\",\n" +
                "\t\t\"path\": \"/articles/centos-reset-password\",\n" +
                "\t\t\"url\": \"/articles/Linux reset password.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"Windows Server 大量文件解密性能分析\",\n" +
                "\t\t\"path\": \"/articles/disk-io-performance\",\n" +
                "\t\t\"url\": \"/articles/Windows Server 大量文件解密性能分析.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"path\": \"/maven\",\n" +
                "\t\t\"title\": \"Maven\",\n" +
                "\t\t\"icon\": \"Folder\",\n" +
                "\t\t\"children\": [{\n" +
                "\t\t\t\"title\": \"Maven 常用命令\",\n" +
                "\t\t\t\"path\": \"/articles/maven-common-command\",\n" +
                "\t\t\t\"url\": \"/articles/MavenCommonCommand.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"title\": \"Maven 分不同的环境打包不同目录下的配置文件\",\n" +
                "\t\t\t\"path\": \"/articles/maven-separate-env-profile\",\n" +
                "\t\t\t\"url\": \"/articles/MavenSeparateEnvProfile.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t}]\n" +
                "\t},{\n" +
                "\t\t\"path\": \"/algorithm\",\n" +
                "\t\t\"title\": \"算法\",\n" +
                "\t\t\"icon\": \"Folder\",\n" +
                "\t\t\"children\": [{\n" +
                "\t\t\t\"title\": \"高矮个子排队\",\n" +
                "\t\t\t\"path\": \"/articles/Queue-tall-short\",\n" +
                "\t\t\t\"url\": \"/articles/高矮个子排队.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"title\": \"找终点\",\n" +
                "\t\t\t\"path\": \"/articles/find-endpoint\",\n" +
                "\t\t\t\"url\": \"/articles/找终点.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"title\": \"智能成绩表\",\n" +
                "\t\t\t\"path\": \"/articles/intel\",\n" +
                "\t\t\t\"url\": \"/articles/智能成绩表.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"title\": \"计算三叉搜索树的高度\",\n" +
                "\t\t\t\"path\": \"/articles/hight-of-three-branches-tree\",\n" +
                "\t\t\t\"url\": \"/articles/计算三叉搜索树的高度.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"title\": \"数大雁\",\n" +
                "\t\t\t\"path\": \"/articles/goose-count\",\n" +
                "\t\t\t\"url\": \"/articles/GooseCount.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t}]\n" +
                "\t},{\n" +
                "\t\t\"path\": \"/database\",\n" +
                "\t\t\"title\": \"数据库\",\n" +
                "\t\t\"icon\": \"Folder\",\n" +
                "\t\t\"children\": [{\n" +
                "\t\t\t\"title\": \"Docker 安装 MySQL\",\n" +
                "\t\t\t\"path\": \"/articles/docker-mysql\",\n" +
                "\t\t\t\"url\": \"/articles/Docker-MySQL.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"title\": \"MySQL集群\",\n" +
                "\t\t\t\"path\": \"/articles/mysql-cluster\",\n" +
                "\t\t\t\"url\": \"/articles/MySQL集群.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"title\": \"MySQL 主从同步异常-更新主键\",\n" +
                "\t\t\t\"path\": \"/articles/mysql-m-s-update-pk\",\n" +
                "\t\t\t\"url\": \"/articles/MySQL 主从同步异常.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t}]\n" +
                "\t},{\n" +
                "\t\t\"path\": \"/front-end\",\n" +
                "\t\t\"title\": \"前端\",\n" +
                "\t\t\"icon\": \"Folder\",\n" +
                "\t\t\"children\": [{\n" +
                "\t\t\t\"path\": \"/articles/vite-config-server\",\n" +
                "\t\t\t\"title\": \"vite.config server 配置详解\",\n" +
                "\t\t\t\"url\": \"/articles/vite.config.js server 配置详解.md\",\n" +
                "\t\t\t\"icon\": \"Document\",\n" +
                "\t\t\t\"component\": \"MdViewer\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"path\": \"/about\", \n" +
                "\t\t\t\"title\": \"Vue Anki\", \n" +
                "\t\t\t\"component\": \"AboutView\" ,\n" +
                "\t\t\t\"icon\": \"Document\"\n" +
                "\t\t}]\n" +
                "\t},{\n" +
                "\t\t\"title\": \"富文本转PDF\",\n" +
                "\t\t\"path\": \"/articles/html-to-pdf\",\n" +
                "\t\t\"url\": \"/articles/HtmlToPdf.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"Skywalking\",\n" +
                "\t\t\"path\": \"/articles/skywalking\",\n" +
                "\t\t\"url\": \"/articles/Skywalking.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"服务器主板可以单独升级吗？有什么影响？\",\n" +
                "\t\t\"path\": \"/articles/server-board-upgrade\",\n" +
                "\t\t\"url\": \"/articles/BoardUpGrade.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t  \"title\": \"服务发布策略\",\n" +
                "\t  \"path\": \"/articles/application-deploy-strategy\",\n" +
                "\t  \"url\": \"/articles/ApplicationDeployStrategy.md\",\n" +
                "\t  \"icon\": \"Document\",\n" +
                "\t  \"component\": \"MdViewer\"\n" +
                "  },\n" +
                "\t{\n" +
                "\t\t\"title\": \"研发及上线规范\",\n" +
                "\t\t\"path\": \"/articles/code-and-deploy\",\n" +
                "\t\t\"url\": \"/articles/CodeAndDeploy.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"基于雪花算法的ID生成器\",\n" +
                "\t\t\"path\": \"/articles/snowflake-id-gen\",\n" +
                "\t\t\"url\": \"/articles/SnowflakeId.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t  \"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"消息队列（MQ）中间件选型比较\",\n" +
                "\t\t\"path\": \"/articles/mq-compare\",\n" +
                "\t\t\"url\": \"/articles/MQ.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"ElasticSearchStack 安装于使用\",\n" +
                "\t\t\"path\": \"/articles/es-stack-install\",\n" +
                "\t\t\"url\": \"/articles/ElasticStack.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"Postman 使用\",\n" +
                "\t\t\"path\": \"/articles/postman\",\n" +
                "\t\t\"url\": \"/articles/Postman.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"Docker 安装与使用\",\n" +
                "\t\t\"path\": \"/articles/docker\",\n" +
                "\t\t\"url\": \"/articles/docker.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"Docker 私服的搭建与使用\",\n" +
                "\t\t\"path\": \"/articles/docker-registry-install-and-use\",\n" +
                "\t\t\"url\": \"/articles/Docker 私服的搭建与使用.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},{\n" +
                "\t\t\"title\": \"Docker 手动安装\",\n" +
                "\t\t\"path\": \"/articles/docker-offline-install\",\n" +
                "\t\t\"url\": \"/articles/Docker 手动安装.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"title\": \"Docker 安装 Nginx\",\n" +
                "\t\t\"path\": \"/articles/docker-nginx\",\n" +
                "\t\t\"url\": \"/articles/docker-nginx.md\",\n" +
                "\t\t\"icon\": \"Document\",\n" +
                "\t\t\"component\": \"MdViewer\"\n" +
                "\t}\n" +
                "]\n";
        List<ArticleDTO> articleDTOS = JSON.parseArray(str, ArticleDTO.class);
        for (ArticleDTO articleDTO : articleDTOS) {
            if (CollectionUtil.isEmpty(articleDTO.getChildren())) {
                saveArticle(articleDTO);
            } else {
                ColumnInfo entity = saveColumn(articleDTO);
                List<ArticleDTO> children = articleDTO.getChildren();
                if (CollectionUtil.isNotEmpty(children)) {
                    for (ArticleDTO child : children) {
                        Article article = saveArticle(child);
                        saveArticleColumn(article, entity);
                    }
                }
            }
        }
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
