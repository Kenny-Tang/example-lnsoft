package com.redjujubetree.example.huawei;


import java.util.*;

public class GuessWord {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String[] split = scanner.nextLine().split(",");
		Set<String> words = new HashSet<>();
		// 将字面排序去重后放入集合
		for (String s : split) {
			String word = sortAndDistinct(s);
			words.add(word);
		}
		String[] res = scanner.nextLine().split(",");

		List<String> result = new ArrayList<>();
		// 将结果去重排序后，看看在集合中是否存在，如果存在将结果记录下来
		for (String str : res) {
			String word = sortAndDistinct(str);
			if (words.contains(word)) {
				// 如果猜中，在放入集合等待输出
				result.add(str);
			}
		}
		if (result.size() == 0) {
			System.out.println("not found");
		} else {
			System.out.println(String.join(",", result));
		}
	}

	// 将单词去重排序
	private static String sortAndDistinct(String s) {
		char[] chars = s.toCharArray();
		String word = "";
		// 将单词字母排序
		Arrays.sort(chars);
		// 将单词字母去重
		for (char c : chars) {
			if (word.contains(String.valueOf(c))) {
				continue;
			}
			word += c;
		}
		return word;
	}
}
