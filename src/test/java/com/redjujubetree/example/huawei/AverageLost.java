package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.List;

public class AverageLost {
	static Input input ;
	static {
		input = new Input("2\n" +
				"0 0 100 2 2 99 0 2");
	}

	public static void main(String[] args) {
		// 平均值
		int averageLost = Integer.parseInt(input.nextLine());
		String[] larr = input.nextLine().split(" ");

		// 接口波动数组
		int[] lost = new int[larr.length];
		// 前缀和数组
		int[] preSum = new int[larr.length + 1];

		int minval = Integer.MAX_VALUE;
		for (int i = 0; i < larr.length; i++) {
			lost[i] = Integer.parseInt(larr[i]);
			preSum[i+1] = preSum[i] + lost[i];
			minval = Math.min(minval, lost[i]) ;
		}
		// 如果最小值都大于平均值直接返回
		if (minval > averageLost) {
			System.out.println("NULL");
			return;
		}

		int len = lost.length;
		List<int[]> results = new ArrayList<>();
		// 从最长长度开始穷举
		for (int length = len; length > 0; length--) {
			int left = 0;
			int right = left + length - 1;
			for (; right < len; right++, left++) {
				int sum = preSum[right+1] - preSum[left];
				if (sum <= averageLost * length) {
					results.add(new int[]{left, right});
				}
			}
			// 如果找到结束遍历
			if (results.size() > 0) {
				break;
			}
		}
		for (int[] result : results) {
			System.out.print(result[0] + "-" + result[1] + " ");
		}
	}
}
