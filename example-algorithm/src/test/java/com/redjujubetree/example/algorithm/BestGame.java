package com.redjujubetree.example.algorithm;

import java.util.Arrays;

public class BestGame {
	static Input input;
	static {
		input = new Input("6 30\n" +
				"81 87 47 59 81 18");
	}
	static int[][] dp = new int[55][55];
	static int INIT = Integer.MAX_VALUE / 2;

	public static void main(String[] args) {
		String[] ss = input.nextLine().split(" ");
		int n = Integer.parseInt(ss[0]);
		int d = Integer.parseInt(ss[1]);

		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[0].length; j++) {
				dp[i][j] = INIT;
			}
		}

		for (int i = 0; i <= n; i++) {
			dp[i][0] = 0;
		}

		int[] team = new int[n+1];
		String[] ts = input.nextLine().split(" ");
		for (int i = 1; i <= n; i++) {
			team[i] = Integer.parseInt(ts[i-1]);
		}
		Arrays.sort(team, 1, n+1);

		int ans = INIT;
		int maxPairs = 0;
		for (int i = 2; i <= n; i++) {
			for (int j = 1; j <= n/2; j++) {
				dp[i][j] = dp[i-1][j];
				if (team[i] - team[i - 1] <= d) {
					dp[i][j] = Math.min(dp[i][j], dp[i-2][j-1] + team[i] - team[i - 1]);
					if (dp[i][j] != INIT && j >= maxPairs ) {
						if (j > maxPairs) {
							maxPairs = j;
							ans = dp[i][j];
						} else {
							ans = Math.max(ans, dp[i][j]);
						}
					}
				}
			}
		}
		if (ans != INIT) {
			System.out.println(ans);
		} else {
			System.out.println(-1);
		}
	}
}
