package com.redjujubetree.response;

import lombok.Getter;

@Getter
public enum RespCodeEnum {
	SUCCESS(0, "请求成功"),
	FAIL(999, "请求失败"),
	NO_AUTH(401, "没有权限"),
	NO_LOGIN(403, "未登录"),
	NO_DATA(404, "没有数据"),
	PARAM_ERROR(400, "参数错误"),
	NO_PERMISSION(403, "没有权限"),
	NO_TOKEN(401, "没有token"),
	TOKEN_EXPIRED(401, "token过期"),
	SYSTEM_ERROR(500, "系统异常");
	;
	private int code;
	private String message;
	RespCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
