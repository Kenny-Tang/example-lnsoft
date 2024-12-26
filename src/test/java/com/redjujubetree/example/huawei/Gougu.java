package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Gougu {

	static Input input;
	static {
		input = new Input("1\n20");
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		int m = Integer.parseInt(input.nextLine());

		List<Triple> triples = new ArrayList<>();
		for (int i = 1; i * i < m; i++) {
			for (int j = 1; j < i; j++) {
				// i j 一个奇数 一个偶数
				if ((i - j) % 2 == 1 && gcd(j, i) == 1) {
					int a = i*i + j*j;
					int b = 2 * i * j;
					int c = i*i - j*j;
					if (a < m && b < m && c < m && a > n && b > n && c > n) {
						triples.add(new Triple(a, b, c));
					}
				}
			}
		}
		Collections.sort(triples);
		for (Triple triple : triples) {
			System.out.println(triple);
		}
	}

	public static int gcd(int big, int less) {
		int leftval = big % less;
		if (leftval == 0) {
			return less;
		}
		return gcd(less, leftval);
	}

	static class Triple implements Comparable<Triple>{
		int a;
		int b;
		int c;
		public Triple(int a, int b, int c) {
			int[] x =new int[]{a,b,c};
			Arrays.sort(x);
			this.a = x[0];
			this.b = x[1];
			this.c = x[2];
		}

		public int compareTo(Triple o) {
			if (this.a == o.a) {
				if (this.b == o.b) {
					return this.c - o.c;
				}
				return this.b - o.b;
			}
			return this.a - o.a;
		}

		public String toString() {
			return a + " " + b + " " + c;
		}
	}

}
