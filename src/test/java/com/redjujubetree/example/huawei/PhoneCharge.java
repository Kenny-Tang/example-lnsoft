package com.redjujubetree.example.huawei;

public class PhoneCharge {
	static Input input;
	static {
		input = new Input("15\n" +
				"10 20 30 40 60 60 70 80 90 150");
	}

	public static void main(String[] args) {
		int money = Integer.parseInt(input.nextLine());
		String[] messages = input.nextLine().split(" ");
		int msg[] = new int[messages.length+1];

		for (int i = 1; i <= messages.length; i++) {
			msg[i] = Integer.parseInt(messages[i-1]);
		}
		int[] dp = new int[money + 1];
		for (int i = 1; i < msg.length; i++) {
			for (int j = i; j <= money; j++) {
				dp[j] = Math.max(dp[j], dp[j - i] + msg[i]);
			}
		}
		System.out.println(dp[money]);
	}
}
