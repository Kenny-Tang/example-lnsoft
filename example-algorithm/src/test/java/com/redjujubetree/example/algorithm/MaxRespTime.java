package com.redjujubetree.example.algorithm;

public class MaxRespTime {
	static Input input ;
	static {
		input = new Input("2\n" +
				"0 255\n" +
				"200 60");
		input = new Input("3\n" +
				"0 20\n" +
				"1 10\n" +
				"8 20");
	}

	public static void main(String[] args) {

		int n = Integer.parseInt(input.nextLine());
		int result = Integer.MAX_VALUE;
		for (int i = 0; i < n; i++) {
			String[] ss = input.nextLine().split(" ");
			int startTime = Integer.parseInt(ss[0]);
			int respTime = Integer.parseInt(ss[1]);
			if (respTime < 128) {
				result = Math.min(result, startTime + respTime);
			} else {
				int mant = respTime & 15; // 取得后四位
				int exp = respTime >> 4 & 7; // 取得 5~7位
				int resp = (mant | 0x10) << (exp + 3);
				result = Math.min(result, startTime + resp);
			}
		}
		System.out.println(result);
	}
}
