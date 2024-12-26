package com.redjujubetree.example.huawei;

import java.util.*;

public class KStringChar {
	static Input input;
	static {
		input = new Input("AAAAHHHBBCDHHHH\n" +
				"3");
		input = new Input("AABAAA\n" +
				"2");
		input = new Input("ABC\n" +
				"4");
		input = new Input("ABC\n" +
				"2");
	}

	public static void main(String[] args) {
		String str = input.nextLine() + "#";
		int k = Integer.parseInt(input.nextLine());

		Map<Character, Integer> map = new HashMap<>();
		int count = 1;
		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) == str.charAt(i - 1)) {
				count++;
			} else {
				Integer orDefault = map.getOrDefault(str.charAt(i - 1), 0);
				map.put(str.charAt(i - 1), Math.max(orDefault , count));
				count = 1;
			}
		}

		if (map.size() < k) {
			System.out.println(-1);
			return;
		}
		List<Integer> values = new ArrayList<>();
		map.values().forEach(values::add);
		Collections.sort(values, Collections.reverseOrder());

		System.out.println(values.get(k-1));
	}
}
