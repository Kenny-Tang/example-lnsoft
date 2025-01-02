package com.redjujubetree.huawei;

public class SubStringII {
	static Input input;
	static {
		input = new Input("ab\n" +
				"aabcd\n" +
				"1") ;
		input = new Input("abc\n" +
				"dfs\n" +
				"10");
	}

	public static void main(String[] args) {
		String s1 = input.nextLine();
		String s2 = input.nextLine();
		int k = Integer.parseInt(input.nextLine());
		int[] chacount = new int[128];
		for (int i = 0; i < s1.length(); i++) {
			chacount[s1.charAt(i)]++;
		}
		int[] windowcount = new int[128];

		int left = 0;
		int right = s1.length()+k;
		if (s2.length() < s1.length()) {
			System.out.println(-1);
			return;
		}

		// init window
		for (int i = 0; i < right && i < s2.length(); i++) {
			windowcount[s2.charAt(i)]++;
		}

		if (isSame(chacount, windowcount)) {
			System.out.println(-1);
			return;
		}

		while (right < s2.length()) {
			if (isSame(chacount, windowcount)) {
				System.out.println(left);
				return;
			}
			windowcount[s2.charAt(left)]--;
			left++;
			windowcount[s2.charAt(right)]++;
			right++;
		}
		System.out.println(-1);
	}

	public static boolean isSame(int[] chacount, int[] windowcount) {
		for (int i = 0; i < chacount.length; i++) {
			if (windowcount[i] >= chacount[i]) {
				continue;
			}
			return false;
		}
		return true;
	}

}
