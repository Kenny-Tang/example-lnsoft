package com.redjujubetree.huawei;

public class ConsectiveArr {
	static Input input;
	static {
		input = new Input("3 7\n" +
				"3 4 7");
		input = new Input("10 10000\n" +
				"1 2 3 4 5 6 7 8 9 10");
	}

	public static void main(String[] args) {
		String[] ss = input.nextLine().split(" ");
		int length = Integer.parseInt(ss[0]);
		int sum = Integer.parseInt(ss[1]);

		String[] arr = input.nextLine().split(" ");
		int[] intArr = new int[length];
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < length; i++) {
			intArr[i] = Integer.parseInt(arr[i]);
			min = Math.min(min, intArr[i]);
		}
		if (min >= sum) {
			int count = 0;
			for (int i = 1; i <= length; i++) {
				count += i ;
			}
			System.out.println(count);
			return;
		}

		int left = 0;
		int right = 0;
		int result = 0;
		int windowSum = intArr[0];
		while (right < length) {
			if (windowSum>=sum) {
				result += length - right + 1;
				windowSum -= intArr[left];
				left++;
				continue;
			}
			windowSum += intArr[right];
			right++;
		}

		System.out.println(result);

	}
}
