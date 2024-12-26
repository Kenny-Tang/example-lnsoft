package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.List;

public class ArrayConcat {

	static Input input;
	static {
		input = new Input("4\n" +
				"3\n" +
				"1,2,3,4,5,6\n" +
				"1,2,3\n" +
				"1,2,3,4");
	}

	public static void main(String[] args) {
		int subLen = Integer.parseInt(input.nextLine());
		int n = Integer.parseInt(input.nextLine());
		List<List<String>> list = new ArrayList<>();
		List<String> results = new ArrayList<>();

		int maxLen = 0;
		for (int i = 0; i < n; i++) {
			List<String> lines = new ArrayList<>();
			String[] split = input.nextLine().split(",");
			for (int j = 0; j < split.length; j++) {
				lines.add(split[j]);
			}
			maxLen = Math.max(maxLen, lines.size());
			list.add(lines);
		}
		int subIndex = 0;
		while (subIndex < maxLen) {
			for (int i = 0; i < list.size(); i++) {
				List<String> line = list.get(i);
				int start = subIndex;
				int end = subIndex + subLen;
				while (start < end && start < line.size()) {
					results.add(line.get(start++));
				}
			}
			subIndex += subLen;
		}

		System.out.println(String.join(",", results));
	}
}
