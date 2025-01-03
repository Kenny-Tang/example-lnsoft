package com.redjujubetree.huawei;

import java.util.Scanner;

public class EnhanceStrStr {

	public static String haystack = "";
	public static String needle = "" ;
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		haystack = scanner.nextLine();
		needle = scanner.nextLine();
		int result = -1;
		for (int i = 0; i < haystack.length(); i++) {
			if ((result = match(i)) > 0) {
				break;
			}
		}
		System.out.println(result);
	}

	public static int match(int start) {
		int index = 0;
		for (int i = start; i < haystack.length() && index < needle.length(); i++, index++) {
			if (haystack.charAt(i) == needle.charAt(index)) {
				// 如果相等则继续
				continue;
			}
			// 匹配开始
			if (needle.charAt(index) == '[') {
				index++;
				boolean has = false;
				// ] 匹配结束
				while (needle.charAt(index) != ']') {
					// 纯在匹配成功
					if (needle.charAt(index) == haystack.charAt(i)) {
						has = true;
					}
					index++;
				}
				if (!has) {
					// 匹配失败
					return -1;
				}
			} else {
				// 不相等，不匹配
				return -1;
			}
		}
		// 如果needle 没有遍历完成 则无效
		if (index != needle.length()) {
			return -1;
		}
		return start;
	}
}
