package com.redjujubetree.example.algorithm;

public class LongestSubString {
	static Input input ;
	static {
		input = new Input("alolobo\n" +
				"looxdolx\n" +
				"bcbcbc");
	}

	public static void main(String[] args) {

		maxSubstring(input.nextLine());
		maxSubstring(input.nextLine());
		maxSubstring(input.nextLine());
	}

	private static void maxSubstring(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == 'o') {
				count++;
			}
		}
		if (count % 2 == 0) {
			System.out.println(s.length());
		} else {
			System.out.println(s.length() - 1);
		}
	}
}
