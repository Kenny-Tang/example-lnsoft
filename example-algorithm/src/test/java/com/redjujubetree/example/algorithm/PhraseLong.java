package com.redjujubetree.example.algorithm;

import java.util.*;

public class PhraseLong {
	static Input input;
	static {
		input = new Input("0\n" +
				"6\n" +
				"word\n" +
				"dd\n" +
				"da\n" +
				"dc\n" +
				"dword\n" +
				"d");
		input = new Input("4\n" +
				"6\n" +
				"Word\n" +
				"dd\n" +
				"da\n" +
				"dc\n" +
				"dword\n" +
				"d");
	}

	public static void main(String[] args) {
		int index = Integer.parseInt(input.nextLine());

		int count = Integer.parseInt(input.nextLine());
		Map<Character, List<CString>> map = new HashMap<>() ;

		String first = "";
		for (int i = 0; i < count; i++) {
			String word = input.nextLine();
			if (index == i) {
				first = word;
				continue;
			}
			List<CString> orDefault = map.getOrDefault(word.charAt(0), new ArrayList<>());
			orDefault.add(new CString(word));
			map.put(word.charAt(0), orDefault);
		}

		for (Map.Entry<Character, List<CString>> entry : map.entrySet()) {
			List<CString> value = entry.getValue();
			Collections.sort(value);
		}

		Character ch = first.charAt(first.length() - 1);
		StringBuilder sb = new StringBuilder(first);
		while (ch != null) {
			List<CString> cStrings = map.get(ch);
			if (cStrings == null || cStrings.size() == 0) {
				break;
			}
			CString remove = cStrings.remove(0);
			sb.append(remove.value);
			ch = remove.value.charAt(remove.value.length() - 1);
		}
		System.out.println(sb.toString());

	}
	static class CString implements Comparable<CString> {
		public String value;
		public CString(String value) {
			this.value = value;
		}
		public int compareTo(CString o) {
			if (this.value.charAt(0) != o.value.charAt(0)) {
				return	this.value.compareTo(o.value);
			}
			if (this.value.length() != o.value.length()) {
				return o.value.length() - this.value.length();
			}
			return this.value.compareTo(o.value);
		}

	}
}
