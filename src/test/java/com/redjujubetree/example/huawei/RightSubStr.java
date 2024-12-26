package com.redjujubetree.example.huawei;

public class RightSubStr {

	static Input input;
	static {
		input = new Input("D\n" +
				"abaca123D");
	}

	public static void main(String[] args) {
		char ch = input.nextLine().charAt(0);
		String s = input.nextLine();
		int left = 0;
		int right = 0;
		int[] charcount = new int[128];
		int max = 0;
		boolean flag = true;
		while (right < s.length()) {
			if (ch == s.charAt(right)) {
				charcount = new int[128];
				right++;
				left = right;
			} else {
				if (flag) {
					charcount[s.charAt(right)]++;
				}
				if (charcount[s.charAt(right)] > 2) {
					charcount[s.charAt(left)]--;
					left++;
					flag = false;
					continue;
				}
				flag = true;
				right++;
				max = Math.max(max, right - left);
			}
		}
		System.out.println(max);
	}
}
