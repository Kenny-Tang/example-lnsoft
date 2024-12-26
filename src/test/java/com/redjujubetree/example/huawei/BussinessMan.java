package com.redjujubetree.example.huawei;

import java.util.Scanner;

public class BussinessMan {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int count = scanner.nextInt();
		int days = scanner.nextInt();
		// 每件商品的最大持有数量
		int[] holdCnt = new int[days];
		for (int i = 0; i < holdCnt.length; i++) {
			holdCnt[i] = scanner.nextInt();
		}
		// 商品每天的价格
		int[][] prices = new int[count][days];
		int sum = 0;
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < days; j++) {
				prices[i][j] = scanner.nextInt();
				if (j > 0) {
					// 如果大于昨天的价格表示可以昨天买入今天卖出
					if (prices[i][j] > prices[i][j - 1]) {
						int diff = prices[i][j] - prices[i][j - 1];
						sum += holdCnt[i] * diff;
					}
				}
			}
		}
		System.out.println(sum);
	}
}
