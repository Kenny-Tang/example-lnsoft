package com.redjujubetree.example.huawei;

import java.util.*;

public class AssociatedWords {

	static Map<Character, TreeSet<String>> map = new HashMap<>();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		String[] split = str.split("[^a-zA-Z]");
		for (String word : split) {
			if (word.length() > 0) {
				// 将单词放入集合
				map.computeIfAbsent(word.charAt(0), k -> new TreeSet<>()).add(word);
			}
		}
		String input = scanner.nextLine();
		TreeSet<String> strings = map.get(input.charAt(0));
		List<String> result = new ArrayList<>();
		for (String word : strings) {
			if (word.startsWith(input)) {
				result.add(word);
			}
		}
		// TreeSet 为已经排序的集合，无需再次处理
		if (result.size() > 0) {
			System.out.println(String.join(" ", result));
		} else {
			System.out.println(input);
		}
	}
}
