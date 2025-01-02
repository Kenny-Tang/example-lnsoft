package com.redjujubetree.huawei;

import java.util.*;

public class StringReplace {
	static Input input;

	static List<Set<Character>> sets = new ArrayList<>();
	static List<Character> lc = new ArrayList<>();
	static List<Character> uc = new ArrayList<>();
	static {
		input = new Input("(abd)demand(fb)()for");
		input = new Input("()happy(xyz)new(wxy)year(t)");
		input = new Input("()abcdefgAC(a)(Ab)(C)");

		for (int i = 'a'; i <= 'z'; i++) {
			lc.add((char)i);
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			uc.add((char)i);
		}
	}
	public static void main(String[] args) {
		String s = input.nextLine();

		boolean reg = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if ('(' == s.charAt(i)) {
				reg = true;
				continue;
			}
			if (')' == s.charAt(i)) {
				reg = false;
				continue;
			}
			if (!reg) {
				sb.append(s.charAt(i));
			}
		}
		if (sb.length() == 0) {
			System.out.println(0);
			return;
		}
		// 分割
		dealReg(s);
		// 合并
		while (mergeEqual());

		// 替换
		for (int i = 0; i < sets.size(); i++) {
			Set<Character> set = sets.get(i);
			if (set.isEmpty()) continue;
			List<Character> temp = new ArrayList<>(set);
			Collections.sort(temp);
			char tar = temp.get(0);
			for (int j = 0; j < sb.length(); j++) {
				if (set.contains(sb.charAt(j))) {
					sb.setCharAt(j, tar);
				}
			}
		}

		System.out.println(sb.toString());
	}

	public static boolean mergeEqual() {
		int size = sets.size();
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				if (canMerge(sets.get(i), sets.get(j))) {
					sets.get(i).addAll(sets.get(j));
					sets.remove(j);
					return true;
				}
			}
		}
		return false;
	}

	public static boolean canMerge(Set<Character> set1, Set<Character> set2) {
		for (int i = 0; i < lc.size(); i++) {
			if ((set1.contains(lc.get(i)) || set1.contains(uc.get(i)))
					&& (set2.contains(lc.get(i)) || set2.contains(uc.get(i)))) {
				return true;
			}
		}
		return false;
	}

	public static void dealReg(String s) {
		int start = s.indexOf("(");
		if (start == -1) {
			return;
		}
		int end = s.indexOf(")");
		String reg = s.substring(start+1, end);


		Set<Character> set = new HashSet<>();
		for (int i = 0; i < reg.length(); i++) {
			char currCh = reg.charAt(i);
			set.add(currCh);
		}
		sets.add(set);

		String substring = s.substring(end + 1);

		dealReg(substring);
	}
}
