package com.redjujubetree;

import cn.hutool.core.util.StrUtil;

public class KStringUtils extends StrUtil {

	/**
	 *  Converts a cameCase or PascalCase string to kebab-case.
	 *  <p>
	 *      Examples:
	 *     <ul>
	 *         <li>"helloWorld" → "hello-world"</li>
	 *         <li>"getHTTPStatus" → "get-http-status"</li>
	 *     </ul>
	 *  </p>
	 * @param input the cameCase or PascalCase string
	 * @return the kebab string of the input string, or null if the input is null
	 */
	public static String toKebabCase(String input) {
		if (input == null) return null ;

		// Insert a dash between a lowercase/number and an uppercase letter
		// e.g., "userName" → "user-Name"
		String kebab = input.replaceAll("([a-z0-9])([A-Z])", "$1-$2");

		// Insert a dash between a sequence of uppercase letters followed by a capitalized word
		// e.g., "getHTTPResponse" → "get-HTTP-Response"
		kebab = kebab.replaceAll("([A-Z]+)([A-Z][a-z])", "$1-$2");

		return kebab.toLowerCase();
	}
}
