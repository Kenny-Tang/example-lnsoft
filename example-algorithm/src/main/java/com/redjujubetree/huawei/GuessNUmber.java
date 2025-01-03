package com.redjujubetree.huawei;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GuessNUmber {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		int n = Integer.parseInt(scanner.nextLine());
		List<String[]> guesses = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			String s = scanner.nextLine();
			String[] guess = s.split(" ");
			guesses.add(guess);
		}
		List<Integer> results = new ArrayList<>();
		for (int i = 1000; i < 10000; i++) {
			for (int ii = 0; ii <= guesses.size() ; ii++) {
				if (ii == guesses.size()) {
					// 如果所有猜测都符合则加入结果集合
					results.add(i);
					continue;
				}
				String result = getString(i+"", guesses.get(ii)[0]);
				if (!result.equals(guesses.get(ii)[1])) {
					break;
				}
			}
			// 出现非唯一结果则结束
			if (results.size() > 1) {
				break;
			}
		}
		if (results.size() == 1) {
			System.out.println(results.get(0));
		} else {
			System.out.println("NA");
		}
	}

	// 返回猜测结果
	private static String getString(String tar, String num) {
		int allRight = 0;
		int numRight = 0;
		char[] tars = tar.toCharArray();
		char[] nums = num.toCharArray();
		for (int j = 0; j < nums.length; j++) {
			if (tar.charAt(j) == num.charAt(j)) {
				allRight++;
				// 猜对的数字修改为特殊字符
				tars[j] = '#';
				nums[j] = '_';
			}
		}
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < tars.length; j++) {
				if (tars[j] == nums[i]) {
					numRight++;
					// 猜对的数字修改为特殊字符
					tars[j] = '#';
					nums[j] = '_';
					break;
				}
			}
		}
		String result = allRight + "A" + numRight + "B";
		return result;
	}
}
