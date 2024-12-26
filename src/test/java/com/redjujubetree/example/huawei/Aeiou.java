package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.HashMap;

public class Aeiou {
	static HashMap<Character, Boolean> map = new HashMap<>();
	static Input input;

	static {
		input = new Input("1\n" +
				"aabeebuu");
//		input = new Input("2\n" +
//				"aeueo");
		 String l = "aeiouAEIOU";
		 for (int i = 0; i < l.length(); i++) {
			 map.put(l.charAt(i), true);
		 }
	}

	public static void main(String[] args) {
		int badNum = Integer.parseInt(input.nextLine());
		String line = input.nextLine();
		String string = " " + line + " ";
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < string.length(); i++) {
			if (!map.getOrDefault(string.charAt(i), false)) {
				list.add(i);
			}
		}

		int max = 0;
		for (int i = 1; i < list.size() && i + badNum < list.size(); i++) {
			int left = i - 1;
			int right = i + badNum;
			if (map.getOrDefault(string.charAt(list.get(left)+1), false) && map.getOrDefault(string.charAt(list.get(right)-1), false)) {
				max = Math.max(max, list.get(right) - list.get(left) - 1);
			}
		}
		System.out.println(max);
	}

}
