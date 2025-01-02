package com.redjujubetree.huawei;

public class YuhuaStone {
	static Input input;
	static {
		input = new Input("10\n" +
				"1 1 1 1 1 9 8 3 7 10");
		input = new Input("4\n" +
				"1 1 2 2");
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		String[] strs = input.nextLine().split(" ");
		int[] weights = new int[strs.length+1];
		int sum = 0;
		for (int i = 0; i < strs.length; i++) {
			weights[i+1] = Integer.parseInt(strs[i]);
			sum += weights[i];
		}
		if (sum % 2 == 1) {
			System.out.println(-1);
			return;
		}
		int target = sum / 2;

		int[][] dp = new int[n+1][target+1];
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[0].length; j++) {
				dp[i][j] = Integer.MAX_VALUE/2;
			}
		}

		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= target; j++) {
				if (j == weights[i]) {
					dp[i][j] = 1;
				} else if (j > weights[i]) {
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j-weights[i]]+1);
				}
 			}
		}
		System.out.println(dp[n][target]);
	}
}
