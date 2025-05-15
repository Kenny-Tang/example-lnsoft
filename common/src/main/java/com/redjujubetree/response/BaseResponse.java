package com.redjujubetree.response;


public class BaseResponse{
	private int code;
	private String message;
	private String traceId;

	private Object data;

	public BaseResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public BaseResponse(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public BaseResponse(RespCodeEnum resp, String message) {
		this.code = resp.getCode();
		this.message = resp.getMessage();
		this.traceId = message;
	}

	public static BaseResponse ofSuccess() {
		return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), "请求成功");
	}

	public static BaseResponse ofSuccess(Object data) {
		BaseResponse response = new BaseResponse(RespCodeEnum.SUCCESS.getCode(), "请求成功");
		response.setData(data);
		return response;
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
