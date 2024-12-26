package com.redjujubetree.example.huawei;

import java.util.HashSet;

public class DrawArea {

	static Input input;
	static {
		input = new Input("2\n" +
				"d 0 2 2 0\n" +
				"d -1 1 1 -1");
		input = new Input("2\n" +
				"d 0 2 2 0\n" +
				"e -1 1 1 -1");
	}

	public static void main(String[] args) {
		Integer count = Integer.parseInt(input.nextLine());
		HashSet<String> set = new HashSet<>();
		for (int i = 0; i < count; i++) {
			String[] ss = input.nextLine().split(" ");
			String type = ss[0];
			int x1 = Integer.parseInt(ss[1]);
			int y1 = Integer.parseInt(ss[2]);
			int x2 = Integer.parseInt(ss[3]);
			int y2 = Integer.parseInt(ss[4]);
			for (int x = x1; x < x2; x++) {
				for (int y = y1; y > y2; y--) {
					String string = new StringBuilder().append(x).append(y).append(x + 1).append(y - 1).toString();
					if ("d".equals(type)) {
						set.add(string);
					}
					if ("e".equals(type)) {
						set.remove(string);
					}
				}
			}
		}
		System.out.println(set.size());
	}

}
