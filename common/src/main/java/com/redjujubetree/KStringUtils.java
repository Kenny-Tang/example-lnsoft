package com.redjujubetree;

import cn.hutool.core.util.StrUtil;

public class KStringUtils extends StrUtil {

	public static String toKebabCase(String input) {
		return input.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase();
	}
}
