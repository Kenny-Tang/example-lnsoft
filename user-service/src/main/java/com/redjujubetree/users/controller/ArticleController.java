package com.redjujubetree.users.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import com.redjujubetree.users.domain.dto.ArticleDTO;
import com.redjujubetree.users.domain.dto.param.HomeViewArticleQueryDTO;
import com.redjujubetree.users.domain.entity.Article;
import com.redjujubetree.users.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
            List<ArticleDTO> articleDTOList = articleService.queryUserArticleList();
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
            Page<Article> articleDTOList = articleService.queryHomeView(query);
            return BaseResponse.ofSuccess(articleDTOList);
        } catch(IllegalArgumentException e){
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
