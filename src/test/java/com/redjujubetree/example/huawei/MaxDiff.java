package com.redjujubetree.example.huawei;

public class MaxDiff {
	static Input input;
	static {
		input = new Input("6\n" +
				"1 -2 3 4 -9 7");
	}

	public static void main(String[] args) {
		int count = Integer.parseInt(input.nextLine());
		String[] strs = input.nextLine().split(" ");

		int[] nums = new int[strs.length+1];
		int sum = 0;
		for (int i = 0; i < count; i++) {
			sum += Integer.parseInt(strs[i]);
			nums[i] = sum;
		}

		int max = nums[count - 1];

		int maxDiff = 0;
		for (int i = 0; i < count-1; i++) {
			int val = Math.abs(nums[i] - (max-nums[i]));
			maxDiff = Math.max(maxDiff, val);
		}

		System.out.println(maxDiff);
	}
}
