package com.redjujubetree.example.huawei;

import java.util.Arrays;

public class Huyang {

	static Input input ;
	static {
		input = new Input("10\n" +
				"3\n" +
				"2 4 7\n" +
				"1");
//		input = new Input("5\n" +
//				"2\n" +
//				"2 4\n" +
//				"1");
//		input = new Input("1\n" +
//				"0\n" +
//				"0\n" +
//				"0");
	}

	public static void main(String[] args) {
		// 树木的总数量
		Integer count = Integer.parseInt(input.nextLine());
		int[] trees = new int[count + 1];
		Arrays.fill(trees, 1);
		// 未成活的数量
		String unlived = input.nextLine();
		// 未成活的树木用 0 表示
		String[] strs = input.nextLine().split(" ");
		for (int i = 0; i < strs.length; i++) {
			int index = Integer.parseInt(strs[i]);
			trees[index] = 0;
		}

		// 补种的数量
		int k = Integer.parseInt(input.nextLine());

		// 窗口的左边界
		int left = 1;
		// 窗口的右边界
		int right = 1;
		// 最大窗口的长度
		int maxLength = 1;
		// 窗口内成活的树木的数量
		int lived = 0;

		while (left <= right && right < trees.length) {
			lived += trees[right];
			// 死掉的数量大于补种的数量，移动窗口左边界
			while (right - left + 1 - lived > k) {
				lived -= trees[left];
				left++;
			}
			maxLength = Math.max(maxLength, right - left + 1);
			right++;
		}

		System.out.println(maxLength);
	}

}
