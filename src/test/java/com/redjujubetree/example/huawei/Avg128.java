package com.redjujubetree.example.huawei;

public class Avg128 {

	static Input input;
	static  {
		input = new Input("129 130 129 130");
		input = new Input("0 0 0 0");
	}
	static int[] array;
	public static void main(String[] args) {
		String[] split = input.nextLine().split(" ");
		array = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			array[i] = Integer.parseInt(split[i]);
		}

		int left = -128;
		int right = 128;
		while (left < right) {
			int mid = (left + right + 1) / 2;
			if (avg(mid) <= 128) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		if (Math.abs(avg(left) - 128) > Math.abs(avg(left + 1) - 128) ) {
			System.out.println(left + 1);
		} else {
			System.out.println(left);
		}
	}

	public static double avg(int k) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += Math.min(Math.max(array[i] + k, 0) , 255);
		}
		return sum / array.length;
	}
}
