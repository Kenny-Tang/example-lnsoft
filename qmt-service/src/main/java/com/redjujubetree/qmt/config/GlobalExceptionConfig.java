package com.redjujubetree.users.config;

import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.ExceptionResponse;
import com.redjujubetree.response.RespCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO 封装为 Spring Boot Starter 进行同一化处理，可增加异常告警处理逻辑
 * 使用 @RestControllerAdvice 可以保证接口返回的是 JSON。
 *
 * 全局异常处理配置
 * 捕获 IllegalArgumentException 和其他异常，返回统一参数校验异常
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.redjujubetree.users.controller") // 限定范围
public class GlobalExceptionConfig {

    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn(e.getMessage(), e);
        return ExceptionResponse.error(RespCodeEnum.PARAM_ERROR.getCode(), e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常请联系管理员处理", e);
        return ExceptionResponse.error(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理", request.getRequestURI());
    }
}
