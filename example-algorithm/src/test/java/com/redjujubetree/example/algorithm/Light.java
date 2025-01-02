package com.redjujubetree.example.algorithm;

import java.util.Scanner;

public class Light {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		// 读取 x 坐标
		int x = scanner.nextInt();
		// 读取 y 坐标
		int y = scanner.nextInt();
		// 读取电厂长度
		int len = scanner.nextInt();
		// 读取最小发电量
		int min = scanner.nextInt();

		// 构建前缀和数组
		int[][] preSum = new int[x+1][y+1];

		for (int i = 1; i <= x; i++) {
			for (int j = 1; j <= y; j++) {
				int val = scanner.nextInt();
				preSum[i][j] = preSum[i-1][j] + preSum[i][j-1] - preSum[i-1][j-1]  + val;
			}
		}

		int count = 0;
		for (int i = len; i < preSum.length; i++) {
			for (int j = len; j < preSum[i].length; j++) {
				// 计算矩阵和
				int sum = preSum[i][j] - preSum[i - len][j] - preSum[i][j - len] + preSum[i - len][j - len];
				if (sum >= min) {
					count++;
				}
			}
		}
		System.out.println(count);
	}
}
