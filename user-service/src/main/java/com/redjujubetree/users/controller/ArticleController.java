package com.redjujubetree.users.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redjujubetree.KStringUtils;
import com.redjujubetree.SystemClock;
import com.redjujubetree.common.CacheUtil;
import com.redjujubetree.common.ThreadPoolUtil;
import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import com.redjujubetree.users.domain.bo.ArticleBO;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
import com.redjujubetree.users.domain.entity.Article;
import com.redjujubetree.users.domain.entity.ColumnInfo;
import com.redjujubetree.users.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/{id}")
    public BaseResponse queryById(@PathVariable("id") Long id) {
        ArticleBO byId = articleService.getArticleById(id);
        if (byId != null) {
            ArticleDTO articleDTO = new ArticleDTO();
            Article article = byId.getArticle();
            BeanUtil.copyProperties(article, articleDTO);
            articleDTO.setId(article.getId().toString());
            articleDTO.setUpdateTime(DateUtil.formatDateTime(article.getUpdateTime()));
            ColumnInfo column = byId.getColumn();
            if (column == null) {
                log.warn("文章 {} 没有对应的栏目", article.getId());
                return BaseResponse.ofSuccess(articleDTO);
            }
            articleDTO.setColumnId(column.getId().toString());
            articleDTO.setCover(column.getCover());
            return BaseResponse.ofSuccess(articleDTO);
        }
        return BaseResponse.ofSuccess(byId);
    }

    @ResponseBody
    @RequestMapping("/list" )
    public BaseResponse list() {
        try {
            List<ArticleDTO> o = CacheUtil.get("/users/article/list");
            if (o != null) {
                log.info("从缓存中获取路由列表: {}", JSON.toJSONString(o));
                if (SystemClock.currentTimeMillis() % 100 > 80) {
                    // 10% 的概率异步更新缓存
                    ThreadPoolUtil.execute(() -> { getRouterList(); });
                }
                return BaseResponse.ofSuccess(o);
            }
            List<ArticleDTO> articleDTOList = getRouterList();
            return BaseResponse.ofSuccess(articleDTOList);
        } catch(IllegalArgumentException e){
            log.warn(e.getMessage(), e);
            return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
        } catch (Exception e) {
            log.error("系统异常请联系管理员处理", e);
            return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
        }
    }

    private List<ArticleDTO> getRouterList() {
        List<ArticleDTO> articleDTOList = articleService.queryUserArticleList();
        if (CollectionUtil.isNotEmpty(articleDTOList)) {
            CacheUtil.put("/users/article/list", articleDTOList, 60 * 10);
        }
        return articleDTOList;
    }

    @RequestMapping("/homeView")
    public BaseResponse homeView(@RequestBody  HomeViewArticleQueryDTO query) {
        try {
            Page o = CacheUtil.get(query.key());
            if (o != null) {
                log.info("从缓存中获取首页文章列表: {}", JSON.toJSONString(o));
                if (SystemClock.currentTimeMillis() % 100 > 80) {
                    // 10% 的概率异步更新缓存
                    ThreadPoolUtil.execute(() -> { getArticlePage(query); });
                }
                return BaseResponse.ofSuccess(o);
            }
            Page articleDTOList = getArticlePage(query);
            return BaseResponse.ofSuccess(articleDTOList);
        } catch(IllegalArgumentException e){
            log.warn(e.getMessage(), e);
            return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
        } catch (Exception e) {
            log.error("系统异常请联系管理员处理", e);
            return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
        }
    }

    private Page getArticlePage(HomeViewArticleQueryDTO query) {
        Page<ArticleDTO> articlePage = articleService.queryHomeView(query);
        if (CollectionUtil.isNotEmpty(articlePage.getRecords())) {
            CacheUtil.put(query.getSearchKey(), articlePage, 60 * 10);
        }
        return articlePage;
    }

    @PostMapping("")
    public BaseResponse uploadArticle(@RequestParam("articleFile") MultipartFile articleFile, @ModelAttribute ArticleDTO article) {
        log.info("上传文章文件: {}", articleFile.getOriginalFilename());
        log.info("文章信息: {}", JSON.toJSONString(article));
        if (KStringUtils.isNotEmpty(article.getId())) {
            articleService.updateArticleById(article);
        } else {
            articleService.saveArticle(articleFile.getOriginalFilename(), article);
        }
        CacheUtil.removeKeyPrefix("hvaqd{searchKey=");
        return BaseResponse.ofSuccess("文章上传成功");
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
