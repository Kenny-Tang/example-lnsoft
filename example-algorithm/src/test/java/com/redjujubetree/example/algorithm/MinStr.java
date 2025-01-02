package com.redjujubetree.example.algorithm;

public class MinStr {

	static  Input input;
	static  {
		input = new Input("bcdefa");
		input = new Input("abcdef");
	}

	public static void main(String[] args) {
		char[] chars = input.nextLine().toCharArray();
		char ch = chars[0];
		int index = 0;
		for (int i = 1; i < chars.length; i++) {
			if (ch >= chars[i]) {
				ch = chars[i];
				index = i;
			}
		}
		chars[index] = chars[0];
		chars[0] = ch;
		System.out.println(new String(chars));
	}
}
