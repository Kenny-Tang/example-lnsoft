package com.redjujubetree.huawei;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AMain {

	private static List<Character> list = new ArrayList<>();
	public static void main(String[] args) {
		try{
			Scanner scanner = new Scanner(System.in);
			String quacks = scanner.nextLine();
			for (int i = 0; i < quacks.length(); i++) {
				// 当前需要发生的音节
				char sound = quacks.charAt(i);
				// 下一个需要发出的音节
				Character nextSound = nextSound(sound);
				int i1 = hasWait(sound);
				if (i1 == -1) {
					// 需要增加大雁数量的情况
					if ('q' == sound) {
						list.add(nextSound);
					} else {
						throw new IllegalArgumentException("-1");
					}
				} else {
					// 更新下一个需要发出的音节
					list.set(i1, nextSound);
				}
			}
			System.out.println(list.size()) ;
		} catch (Exception e) {
			System.out.println(-1);
		}

	}

	// 等待发出该音节的大雁
	private static int hasWait(char c) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(c)) {
				return i;
			}
		}
		return -1;
	}

	// 发出当前音节后，下一个可以发出的音节
	public static Character nextSound(char ch) {
		switch (ch) {
			case 'q':
				return 'u';
			case 'u':
				return 'a';
			case 'a':
				return 'c';
			case 'c':
				return 'k';
			case 'k':
				return 'q';
		}
		throw new IllegalArgumentException("-1");
	}
}
