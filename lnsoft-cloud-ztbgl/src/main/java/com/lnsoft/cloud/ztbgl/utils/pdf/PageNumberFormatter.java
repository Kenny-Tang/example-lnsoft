package com.lnsoft.cloud.ztbgl.utils.pdf;

@FunctionalInterface
public interface PageNumberFormatter {
	/**
	 * 格式化页码
	 *
	 * @param pageNumber 页码
	 * @return 格式化后的页码字符串
	 */
	String format(int pageNumber);
}
