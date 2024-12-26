package com.redjujubetree.example.huawei;

public class Poker {
	static Input input;
	static {
		input = new Input("1,-5,-6,4,3,6,-2");
	}

	public static void main(String[] args) {
		String[] ss = input.nextLine().split(",") ;
		int[] intArr = new int[ss.length];
		for (int i = 0; i < ss.length; i++) {
			intArr[i] = Integer.parseInt(ss[i]);
		}

		int[] dp = new int[intArr.length];
		dp[0] = intArr[0] > 0 ? intArr[0] : 0;
		for (int i = 1; i < intArr.length; i++) {
			if (i < 3) {
				if (dp[i - 1] + intArr[i] > 0) {
					dp[i] = dp[i - 1] + intArr[i];
				} else {
					dp[i] = 0;
				}
			} else {
				if (dp[i - 1] + intArr[i] > dp[i - 3]) {
					dp[i] = dp[i - 1] + intArr[i];
				} else {
					dp[i] = dp[i - 3];
				}
			}
		}
		System.out.println(dp[intArr.length - 1]);
	}
}
