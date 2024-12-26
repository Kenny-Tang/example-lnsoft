package com.redjujubetree.example.huawei;

public class SoftDisk {
	static Input input;
	static {
		input = new Input("3\n" +
				"737270\n" +
				"737272\n" +
				"737288");
		input = new Input("6\n" +
				"400000\n" +
				"200000\n" +
				"200000\n" +
				"200000\n" +
				"400000\n" +
				"400000");
	}
	static int N = 1474560 / 512;
	public static void main(String[] args) {
		int m = Integer.parseInt(input.nextLine()) ;
		int[] w = new int[m+1];
		for (int i = 1; i <= m; i++) {
			w[i] = Integer.parseInt(input.nextLine());
		}

		int[][] dp = new int[m+1][N+1];

		for (int i = 1; i <= m; i++) {
			int weight = (w[i] + 511) / 512;
			for (int j = 1; j <= N; j++) {
				if (j < weight) {
					dp[i][j] = dp[i-1][j];
				} else {
					dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-weight] + w[i]);
				}
			}
		}
		System.out.println(dp[m][N]);
	}

}
