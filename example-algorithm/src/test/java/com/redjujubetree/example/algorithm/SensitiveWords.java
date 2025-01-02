package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.List;

public class SensitiveWords {

	static Input input ;
	static {
		input = new Input("1\n" +
				"password__a12345678_timeout_100");
		input = new Input("2\n" +
				"aaa_password_\"a12_45678\"_timeout__100_\"\"_");
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		int k = Integer.parseInt(input.nextLine());
		String ps = input.nextLine();
		StringBuilder sb = new StringBuilder("");

		for (int i = 0 ; i < ps.length();) {
			if (ps.charAt(i) == '"') {
				// 处理特殊敏感词
				sb.append(ps.charAt(i)) ;
				i++;
				while (ps.charAt(i) != '"') {
					sb.append(ps.charAt(i)) ;
					i++;
				}
				sb.append('"');
				list.add(sb.toString());
				sb = new StringBuilder("");
				i++;
			} else {
				// 出现 _ 表示前边有一个敏感词
				if (ps.charAt(i) == '_') {
					// 如果敏感词长度为 0 不记录
					if (sb.length() > 0) {
						list.add(sb.toString());
					}
					sb = new StringBuilder("");
				} else {
					sb.append(ps.charAt(i));
				}
				i++;
			}
		}
		// 处理最后一个敏感词
		if (sb.length() > 0) {
			list.add(sb.toString());
		}
		if (list.size() > k) {
			list.set(k,"******");
			System.out.println(String.join("_", list));
		} else {
			System.out.println("ERROR");
		}
	}
}
