package com.redjujubetree.huawei;

public class ConsecutiveNumbers {

	static Input input;
	static {
		input = new Input("19801211 5");
	}

	public static void main(String[] args) {
		String[] strings = input.nextLine().split(" ");
		int[] nums = new int[10];
		int n = Integer.parseInt(strings[1]);
		String s = strings[0] ;
		for (int i = 0; i < s.length(); i++) {
			nums[s.charAt(i) - '0']++;
		}

		int left = 1;
		int right = n;
		int numbers[] = new int[10];
		for (int i = 1; i < 6; i++) {
			numbers[i]++;
		}

		while (right < 1000) {
			if (isSame(numbers, nums)) {
				System.out.println(left);
				return;
			}
			int ind = left;
			while (ind > 0) {
				numbers[ind % 10]--;
				ind /= 10;
			}
			left++;
			right++;

			ind = right;
			while (ind > 0) {
				numbers[ind % 10]++;
				ind /= 10;
			}
		}

	}

	public static boolean isSame(int[] arr1, int[] arr2) {
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}
}
