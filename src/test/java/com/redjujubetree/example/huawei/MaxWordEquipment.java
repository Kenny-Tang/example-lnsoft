package com.redjujubetree.example.huawei;

public class MaxWordEquipment {
	static Input input;
	static {
		input = new Input("4\n" +
				"50 20 20 60\n" +
				"90");
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		String[] equipment = input.nextLine().split(" ");
		int[] equipmentArray = new int[n+1];
		for (int i = 1; i <= n; i++) {
			equipmentArray[i] = Integer.parseInt(equipment[i-1]);
		}

		int max = Integer.parseInt(input.nextLine());
		int[][] dp = new int[n+1][max + 1];

		for (int i = 1; i <= n; i++) {
			for (int j = 0; j <=max; j++) {
				if (j-equipmentArray[i] < 0) {
					dp[i][j] = dp[i-1][j];
				} else {
					dp[i][j] = Math.max(dp[i-1][j], dp[i][j-equipmentArray[i]] +equipmentArray[i]);
				}
			}
		}
		System.out.println(dp[n][max]);
	}
}
