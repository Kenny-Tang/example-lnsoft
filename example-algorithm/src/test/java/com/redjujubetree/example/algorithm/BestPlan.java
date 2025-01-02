package com.redjujubetree.example.algorithm;

import java.util.Arrays;

public class BestPlan {
	static Input input;
	static {
		input = new Input("7\n" +
				"1 5 3 6 10 7 13\n" +
				"3");
	}

	static int[] places;
	public static void main(String[] args) {
		int cnt = Integer.parseInt(input.nextLine());
		String[] ss = input.nextLine().split(" ");
		places = new int[ss.length];

		for (int i = 0; i < ss.length; i++) {
			places[i] = Integer.parseInt(ss[i]);
		}
		Arrays.sort(places);

		int treeNum = Integer.parseInt(input.nextLine());

		int left = 0;
		int right = places[places.length - 1];
		int result = 0;
		while (left < right) {
			int mid = (left + right) / 2;
			boolean ok = plant(mid, treeNum);
			if (ok) {
				result = Math.max(result, mid);
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		System.out.println(result);
	}
	public static boolean plant(int dis, int treeNum) {
		int count = 1;
		int index = 0;
		for (int i = 1; i < places.length; i++) {
			if (places[i] - places[index] >= dis) {
				count++;
				index = i;
			}
		}
		return count == treeNum;
	}
}
