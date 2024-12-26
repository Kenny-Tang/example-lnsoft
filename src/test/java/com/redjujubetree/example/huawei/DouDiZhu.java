package com.redjujubetree.example.huawei;

import java.util.*;

public class DouDiZhu {
	// 2 9 J 10 3 4 K A 7 Q A 5 6
	static Map<String, Integer> map = new HashMap<>();
	static {
		map.put("3", 3);
		map.put("4", 4);
		map.put("5", 5);
		map.put("6", 6);
		map.put("7", 7);
		map.put("8", 8);
		map.put("9", 9);
		map.put("10", 10);
		map.put("J", 11);
		map.put("Q", 12);
		map.put("K", 13);
		map.put("A", 14);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String[] ss = in.nextLine().split(" ");
		int[] arr = new int[ss.length];

		for (int i = 0; i < ss.length; i++) {
			// 这里 2 的元素不会被获取， 使用默认值 0 填充， 不会影响结果
			if (map.containsKey(ss[i])) arr[i] = map.get(ss[i]);
		}
		// 对数组排序，方便后面查找
		Arrays.sort(arr);
		boolean[] used = new boolean[arr.length];
		boolean has = false;
		for (int i = 0; i < arr.length - 5; i++) {
			List<Integer> list1 = new ArrayList<>();
			for (int j = i ; j < arr.length; j++) {
				if (used[j]) continue;
				// 第一个元素直接添加
				if (list1.isEmpty()) {
					list1.add(j);
				// 如果下一个元素有可能组成顺子，则添加	
				} else if (arr[list1.get(list1.size() - 1)] == arr[j] - 1) {
					list1.add(j);
				}
			}
			// >= 5 意味着组成了顺子
			if (list1.size() >= 5) {
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < list1.size(); j++) {
					// 标记元素已经使用
					Integer i1 = list1.get(j);
					used[i1] = true;
					sb.append(getRealString(arr[i1]));
				}
				System.out.println(sb.toString().trim());
				has = true;
			}
		}
		if (!has) System.out.println("NO");
	}

	private static String getRealString(Integer i) {
		if (i <= 10) {
			return i + " ";
		} else {
			if (i == 11) return "J ";
			if (i == 12) return "Q ";
			if (i == 13) return "K ";
			if (i == 14) return "A ";
		}
		return "";
	}
}
