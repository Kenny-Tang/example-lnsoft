package com.lnsoft.cloud.ztbgl.common.resp;

import java.util.HashMap;

public class RespResult extends HashMap<String, Object> {

	public static final String CODE_TAG = "code";
	public static final String MSG_TAG = "msg";
	public static final String DATA_TAG = "data";
	public static final String TOTAL_TAG = "total";
	public RespResult() {}
	public RespResult(int code, String msg) {
		this.put(CODE_TAG, code);
		this.put(MSG_TAG, msg);
	}
	public RespResult(int code, String msg, Object data) {
		this.put(CODE_TAG, code);
		this.put(MSG_TAG, msg);
		this.put(DATA_TAG, data);
	}
	public RespResult(int code, String msg, Object data, long total) {
		this.put(CODE_TAG, code);
		this.put(MSG_TAG, msg);
		this.put(DATA_TAG, data);
		this.put(TOTAL_TAG, total);
	}
}
