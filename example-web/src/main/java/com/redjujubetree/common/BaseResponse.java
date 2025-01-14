package com.redjujubetree.common;


public class BaseResponse{
	private int code;
	private String message;
	private String traceId;

	private Object data;

	public BaseResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public BaseResponse(RespCodeEnum resp, String traceId) {}

	public static BaseResponse ofSuccess() {
		return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), "请求成功");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
