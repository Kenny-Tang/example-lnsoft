package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.Scanner;

public class SSS {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String next = scanner.next();
		Integer len = scanner.nextInt();
		System.out.println(next + " " + len);
		dfs(new ArrayList<>(), 0, next, len);
	}

	public static void dfs(ArrayList<String> list, int index, String next, int len) {
		if (index > next.length()) {
			return;
		}
		if (list.size() == len) {
			String join = String.join("", list);
			System.out.println(join);
			return;
		}
		for (int i = index; i < next.length(); i++) {
			ArrayList<String> temp = new ArrayList<>(list);
			String pre = "";
			if (temp.size() > 0) {
				pre = temp.get(temp.size() - 1);
				if (pre.equals(next.charAt(i)+"")) {
					continue;
				} else {
					temp.add(next.charAt(i)+"");
					dfs(temp, i + 1, next, len);
				}
			} else {
				temp.add(next.charAt(i)+"");
				dfs(temp, i + 1, next, len);
			}
		}
	}
}
