package com.redjujubetree.huawei;

public class GameGroup {

	static Input input;
	static {
		input = new Input("1 2 3 4 5 6 7 8 9 10");
	}

	public static void main(String[] args) {
		String[] strs = input.nextLine().split(" ");
		int[] nums = new int[strs.length];
		int sum = 0;
		for (int i = 0; i < strs.length; i++) {
			nums[i] = Integer.parseInt(strs[i]);
			sum += nums[i] ;
		}
		int mindiff = Integer.MAX_VALUE;
		for (int i = 0; i < nums.length; i++) {
			for (int j = i + 1; j < nums.length; j++) {
				for (int k = j + 1; k < nums.length; k++) {
					for (int l = k + 1; l < nums.length; l++) {
						for (int m = l + 1; m < nums.length; m++) {
							int s = nums[i] + nums[j] + nums[k] + nums[l] + nums[m];
							mindiff = Math.min(mindiff, Math.abs(sum - 2*s));
						}
					}
				}
			}
		}
		System.out.println(mindiff);
	}

}
