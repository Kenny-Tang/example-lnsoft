package com.redjujubetree.example.algorithm;

import java.util.*;

public class TreeInfo {
	static Input input;
	static {
		input = new Input("5\n" +
				"b a\n" +
				"c a\n" +
				"d c\n" +
				"e c\n" +
				"f d\n" +
				"c");
	}

	static  Map<String, List<String>> map = new HashMap<>();
	public static void main(String[] args) {
		int count = Integer.parseInt(input.nextLine());

		for (int i = 0; i < count; i++) {
			String[] ss = input.nextLine().split(" ");
			String value = ss[0];
			String key = ss[1];
			map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
		}
		String node = input.nextLine();
		List<String> result = new ArrayList<>();

		dfs(node, result);
		Collections.sort(result);
		for (String s : result) {
			System.out.println(s);
		}
	}

	public static void dfs(String node, List<String> result) {
		List<String> strings = map.get(node);
		if (strings != null && !strings.isEmpty()) {
			for (String str : strings) {
				result.add(str);
				dfs(str, result);
			}
		}
	}

}
