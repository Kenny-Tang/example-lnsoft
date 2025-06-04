package com.redjujubetree.users.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redjujubetree.common.CacheUtil;
import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
import com.redjujubetree.users.domain.entity.Article;
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

    @ResponseBody
    @RequestMapping("/list" )
    public BaseResponse list() {
        try {
            List<ArticleDTO> o = CacheUtil.get("/users/article/list");
            if (o != null) {
                log.info("从缓存中获取路由列表: {}", JSON.toJSONString(o));
                return BaseResponse.ofSuccess(o);
            }
            List<ArticleDTO> articleDTOList = articleService.queryUserArticleList();
            if (CollectionUtil.isNotEmpty(articleDTOList)) {
                CacheUtil.put("/users/article/list", articleDTOList, 60 * 10);
            }
            return BaseResponse.ofSuccess(articleDTOList);
        } catch(IllegalArgumentException e){
            log.warn(e.getMessage(), e);
            return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
        } catch (Exception e) {
            log.error("系统异常请联系管理员处理", e);
            return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
        }
    }

    @RequestMapping("/homeView")
    public BaseResponse homeView(@RequestBody  HomeViewArticleQueryDTO query) {
        try {
            String key = query.key();
            Page<Article> o = CacheUtil.get(key);
            if (o != null) {
                log.info("从缓存中获取首页文章列表: {}", JSON.toJSONString(o));
                return BaseResponse.ofSuccess(o);
            }
            Page<Article> articleDTOList = articleService.queryHomeView(query);
            if (CollectionUtil.isNotEmpty(articleDTOList.getRecords())) {

                CacheUtil.put(key, articleDTOList, 60 * 20);
            }
            return BaseResponse.ofSuccess(articleDTOList);
        } catch(IllegalArgumentException e){
            log.warn(e.getMessage(), e);
            return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
        } catch (Exception e) {
            log.error("系统异常请联系管理员处理", e);
            return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
        }
    }

    @PostMapping("")
    public BaseResponse uploadArticle(@RequestParam("articleFile") MultipartFile articleFile, @ModelAttribute ArticleDTO article) {
        try {
            log.info("上传文章文件: {}", articleFile.getOriginalFilename());
            log.info("文章信息: {}", JSON.toJSONString(article));

            articleService.saveArticle(articleFile.getOriginalFilename(), article);
            return BaseResponse.ofSuccess("文章上传成功");
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage(), e);
            return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
        } catch (Exception e) {
            log.error("系统异常请联系管理员处理", e);
            return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
        }
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
