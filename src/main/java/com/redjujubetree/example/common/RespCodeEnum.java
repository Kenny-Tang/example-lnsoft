package com.redjujubetree.example.common;

import lombok.Getter;

@Getter
public enum RespCodeEnum {
	SUCCESS(0, "请求成功"),
	USER_NOT_LOGIN(1000, "用户未登录"),
	SYSTEM_ERROR(9999, "系统异常"),
	;
	private int code;
	private String message;
	private RespCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
