package com.redjujubetree.example.algorithm;

import java.util.Arrays;

public class SubString {
	static Input input ;
	static {
		input = new Input("abc efghicaibii");
		input = new Input("abc efghicabiii");
	}
	public static void main(String[] args) {
		String[] ss = input.nextLine().split(" ");
		char[] charArray = ss[0].toCharArray();
		Arrays.sort(charArray);
		String str = new String(charArray);

		String tar = ss[1];
		int left = 0;
		int right = left + str.length();

		while (right <= tar.length()) {
			char[] ar = tar.substring(left, right).toCharArray();
			Arrays.sort(ar);
			if (new String(ar).equals(str)) {
				System.out.println(left);
				return;
			}
			left++;
			right++;
		}
		System.out.println(-1);
	}
}
