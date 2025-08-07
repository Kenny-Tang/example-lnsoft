package com.redjujubetree.fs.service;

import java.util.Map;

/**
 * 存储配置接口
 */
public interface StorageConfiguration {
	/**
	 * 获取配置参数
	 */
	Map<String, Object> getConfiguration();

	/**
	 * 验证配置有效性
	 */
	boolean validateConfiguration();
}
