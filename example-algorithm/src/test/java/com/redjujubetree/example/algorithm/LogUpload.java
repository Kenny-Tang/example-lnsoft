package com.redjujubetree.example.algorithm;

import java.util.Scanner;

public class LogUpload {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String[] split = scanner.nextLine().split(" ");

		int[] arr = new int[split.length];

		int max = 0;
		int sum = 0;
		for (int i = 0; i < split.length && sum < 100; i++) {
			arr[i] = Integer.parseInt(split[i]);
			int subTotal = 0;
			// 计算该次上报可以得分的部分
			int next = arr[i];
			if (sum + arr[i] > 100) {
				next = 100 - sum;
			}
			sum += arr[i];
			// 计算该次上报扣分的部分
			for (int ii = 0; ii < i - 1; ii++) {
				subTotal += arr[ii] * ( i - ii - 1);
			}
			// 比较是否取得最大值
			max = Math.max(max, next - subTotal);
		}
		System.out.println(max);
	}
}
