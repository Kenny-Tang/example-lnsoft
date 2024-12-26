package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.List;

public class WaterFlower {

	static Input input ;
	static  {
		input = new Input("f3@d5a8");
	}
	static List<List<String>> list = new ArrayList<>();

	public static void main(String[] args) {
		String string = input.nextLine();

		for (int i = 0; i < string.length(); i++) {
			String substring = string.substring(0, i);
			if (isWaterFlowerNum(substring)) {
				List<String> pre = new ArrayList<>();
				pre.add(substring);
				isOk(string.substring(i), pre);
			}
		}
		if (list.isEmpty()) {
			System.out.println(0);
		} else if (list.size() > 1) {
			System.out.println(-1);
		} else  {
			System.out.println(list.get(0).size());
		}
	}

	public static void isOk(String right, List<String> pre) {
		if (right.isEmpty()) {
			return;
		}
		// 如果右边的也是，则说明整个字符串可以被分割
		if (isWaterFlowerNum(right)) {
			List<String> flist = new ArrayList<>();
			flist.addAll(pre);
			flist.add(right);
			list.add(flist);
		}

		List<String> nextPre = new ArrayList<>();
		nextPre.addAll(pre);

		for (int i = 0; i < right.length(); i++) {
			String substring = right.substring(0, i);
			if (isWaterFlowerNum(substring)) {
				isOk(right.substring(i), nextPre);
			}
		}
	}

	// 判断是否是水仙花数
	public static boolean isWaterFlowerNum(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		int num = 0;
		for (int i = 0; i < str.length(); i++) {
			num += str.charAt(i);
		}
		int old = num;
		int sum = 0;
		while (num > 0) {
			int x = num % 10;
			sum = sum + x*x*x;
			num /= 10;
		}
		return sum == old;
	}
}
