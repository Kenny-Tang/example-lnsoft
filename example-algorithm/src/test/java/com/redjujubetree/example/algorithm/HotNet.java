package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class HotNet {

	static Input input ;
	static {
		input = new Input("news.qq.com\n" +
				"news.sina.com.cn\n" +
				"news.qq.com\n" +
				"news.qq.com\n" +
				"game.163.com\n" +
				"game.163.com\n" +
				"www.huawei.com\n" +
				"www.cctv.com\n" +
				"3\n" +
				"www.huawei.com\n" +
				"www.cctv.com\n" +
				"www.huawei.com\n" +
				"www.cctv.com\n" +
				"www.huawei.com\n" +
				"www.cctv.com\n" +
				"www.huawei.com\n" +
				"www.cctv.com\n" +
				"www.huawei.com\n" +
				"3");
	}

	public static void main(String[] args) {
		TreeMap<Integer, TreeSet<String>> map = new TreeMap<Integer, TreeSet<String>>((o1, o2) -> o2 - o1);
		while (input.hasNextLine()) {
			String line = input.nextLine();
			if (isNumber(line)) {
				Integer top = Integer.parseInt(line);
				ArrayList<String> results = new ArrayList<>();
				outer:
				for (Map.Entry<Integer, TreeSet<String>> entry: map.entrySet()) {
					TreeSet<String> value = entry.getValue();
					for (String item : value) {
						results.add(item);
						if (results.size() == top) {
							break outer;
						}
					}
				}
				System.out.println(String.join(",", results));
			} else {
				Map.Entry<Integer, TreeSet<String>> entryValue = null;
				for (Map.Entry<Integer, TreeSet<String>> entry: map.entrySet()) {
					TreeSet<String> value = entry.getValue();
					if (value.contains(line)) {
						entryValue = entry;
						break;
					}
				}
				if (entryValue == null) {
					TreeSet<String> set = map.getOrDefault(1, new TreeSet<>());
					set.add(line);
					map.put(1, set);
				} else {
					entryValue.getValue().remove(line);
					TreeSet<String> orDefault = map.getOrDefault(entryValue.getKey() + 1, new TreeSet<>());
					orDefault.add(line);
					map.put(entryValue.getKey() + 1, orDefault);
				}
			}
		}
	}
	public static boolean isNumber(String str) {
		return str.matches("[0-9]{0,}");
	}
}
