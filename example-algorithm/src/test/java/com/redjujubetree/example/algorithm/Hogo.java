package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Hogo {
	static Input input;
	static {
		input = new Input("2 1\n" +
				"1 2\n" +
				"2 1");
	}

	public static void main(String[] args) {
		String[] ss = input.nextLine().split(" ");
		int n = Integer.parseInt(ss[0]);
		int m = Integer.parseInt(ss[1]);

		List<Integer> timeList = new ArrayList<>();
		for (int i = 1; i <= n; i++) {
			String[] split = input.nextLine().split(" ");
			int time = Integer.parseInt(split[0]) + Integer.parseInt(split[1]);
			timeList.add(time);
		}
		int waited = 0;
		int cnt = 0;
		for (int i = 0; i < n; i++) {
			Integer i1 = timeList.get(i);
			if (i1 >= waited) {
				cnt++;
				waited =i1+ m;
			}
		}
		System.out.println(cnt);
	}

}
