package com.redjujubetree.example.huawei;

public class StaffNo {
	static Input input;
	static {
		input = new Input("2600 1");
	}

	public static void main(String[] args) {
		String[] split = input.nextLine().split(" ");
		int count = Integer.parseInt(split[0]);
		int Y  = Integer.parseInt(split[1]);
		int a = 1;
		int baseA = 26;
		int baseB = 10;
		for (int i = 0; i < Y; i++) {
			a *= baseA;
		}
		a *= baseB;
		if (a >= count) {
			System.out.println(1);
			return;
		}
		int result = 1;
		while (a < count) {
			a *= 10;
			result++;
		}
		System.out.println(result);
	}
}
