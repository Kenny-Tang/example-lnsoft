package com.redjujubetree.huawei;

import java.util.Arrays;

public class Hotel {

	static Input input;
	static {
		input = new Input("10 4 6\n" +
				"10 9 8 7 6 5 4 3 2 1");
	}

	public static void main(String[] args) {
		String[] ss = input.nextLine().split(" ");
		int n = Integer.parseInt(ss[0]);
		int k = Integer.parseInt(ss[1]);
		int x = Integer.parseInt(ss[2]);

		String[] hs = input.nextLine().split(" ");
		hotel[] hotels = new hotel[n];
		for (int i = 0; i < n; i++) {
			hotels[i] = new hotel(Integer.parseInt(hs[i]), x);
		}

		Arrays.sort(hotels);
		int[] least = new int[k];
		for (int i = 0; i < k; i++) {
			least[i] = hotels[i].price;
		}
		Arrays.sort(least);
		for (int i = 0; i < k; i++) {
			System.out.print(least[i] + " ");
		}
		System.out.println();

	}

	static class hotel implements  Comparable<hotel>{
		public int price;
		public int diff;
		public hotel(int price, int tar) {
			this.price = price;
			this.diff = price - tar;
		}

		public int compareTo(hotel o) {
			int a = Math.abs(diff);
			int b = Math.abs(o.diff);
			if (a == b) {
				return  this.diff - o.diff;
			}
			return a - b;
		}
	}
}
