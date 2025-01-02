package com.redjujubetree.example.algorithm;

import java.util.HashMap;
import java.util.Map;

public class FreeBill {

	static Input input;
	static {
		input = new Input("5\n" +
				"2019-01-01 00:00:00.004\n" +
				"2019-01-01 00:00:00.004\n" +
				"2019-01-01 00:00:01.006\n" +
				"2019-01-01 00:00:01.006\n" +
				"2019-01-01 00:00:01.005\n");
		input = new Input("3\n" +
				"2019-01-01 08:59:00.123\n" +
				"2019-01-01 08:59:00.123\n" +
				"2018-12-28 10:08:00.999");
	}

	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<>();
		int n = Integer.parseInt(input.nextLine());

		Map<String, Integer> result = new HashMap<>();
		for (int i = 0; i < n; i++) {
			String line = input.nextLine();
			String key = line.substring(0, line.lastIndexOf("."));
			Integer value = Integer.parseInt(line.substring(line.lastIndexOf(".") + 1));
			if (map.get(key) == null) {
				map.put(key, value);
				result.put(key, 1);
			} else {
				Integer oldValue = map.get(key);
				if (oldValue == value) {
					result.put(key, result.get(key) + 1);
				} else if (oldValue < value) {

				} else {
					map.put(key, value);
					result.put(key, 1);
				}
			}
		}
		int res = 0;
		for (Integer value : result.values()) {
			res += value;
		}
		System.out.println(res);
	}
}
