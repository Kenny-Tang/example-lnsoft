package com.redjujubetree.example.algorithm;

import java.util.*;

public class NineKeyBoard2 {
	static Input input;
	static Map<Character, char[]> map = new HashMap<>();
	static {
		input = new Input("#2222/22");
		map.put('1', new char[]{',','.'});
		map.put('2', new char[]{'a','b', 'c'});
		map.put('3', new char[]{'d', 'e', 'f'});
		map.put('4', new char[]{'g', 'h', 'i'});
		map.put('5', new char[]{'j', 'k', 'l'});
		map.put('6', new char[]{'m', 'n', 'o'});
		map.put('7', new char[]{'p', 'q', 'r', 's'});
		map.put('8', new char[]{'t', 'u', 'v'});
		map.put('9', new char[]{'w', 'x', 'y', 'z'});
		map.put('0', new char[]{' '});
	}
	public static void main(String[] args) {
		String string = input.nextLine();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				int left =  i;
				int right = i + 1;
				while (right < string.length() && string.charAt(left) == string.charAt(right)) {
					right++;
				}
				String temp = string.substring(left, right);
				i = right - 1;
				list.add(temp);
			} else {
				list.add(string.charAt(i) + "");
			}
		}

		String result = "";
		boolean numberModel = true;
		for (int i = 0; i < list.size(); i++) {
			if ("#".equals(list.get(i))) {
				numberModel = !numberModel;
			} else if ("/".equals(list.get(i))) {

			} else {
				result += getTypingChar(list.get(i), numberModel);
			}
		}
		System.out.println(result);
	}

	public static String getTypingChar(String string, boolean numberModel) {
		if (numberModel) {
			return string;
		}
		char[] chars = map.get(string.charAt(0));
		return chars[(string.length() - 1) % chars.length] + "";
	}


}
