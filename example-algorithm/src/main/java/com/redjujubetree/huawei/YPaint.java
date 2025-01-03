package com.redjujubetree.huawei;

public class YPaint {
	static Input input;
	static  {
		input = new Input("4 10\n" +
				"1 1\n" +
				"2 1\n" +
				"3 1\n" +
				"4 -2");
		input = new Input("2 4\n" +
				"0 1\n" +
				"2 -2");
	}

	public static void main(String[] args) {
		String[] split = input.nextLine().split(" ");
		int n = Integer.parseInt(split[0]);
		int E = Integer.parseInt(split[1]);
		int[][] yx = new int[n][2];
		for (int i = 0; i < n; i++) {
			String[] line = input.nextLine().split(" ");
			yx[i][0] = Integer.parseInt(line[0]);
			yx[i][1] = Integer.parseInt(line[1]);
		}

		int index = 0;
		int absY = 0;
		long sum = 0;
		for (int i = 0; i < E; i++) {
			if (index < yx.length && i == yx[index][0]) {
				absY += yx[index][1];
				index++;
			}
			sum += Math.abs(absY);
		}
		System.out.println(sum);
	}
}
