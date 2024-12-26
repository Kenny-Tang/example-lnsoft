package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxShow {
	static Input input;
	static {
		input = new Input("2\n" +
				"720 120\n" +
				"840 120");
		input = new Input("2\n" +
				"0 60\n" +
				"90 60");
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		List<Show> list = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			String[] ss = input.nextLine().split(" ");
			list.add(new Show(Integer.parseInt(ss[0]), Integer.parseInt(ss[1])));
		}
		Collections.sort(list);
		int empty = 0;
		int max = 0;
		for (Show s : list) {
			if (s.start >= empty) {
				empty = s.end + 15;
				max ++ ;
			}
		}
		System.out.println(max);
	}
	static class Show implements Comparable<Show> {
		public int start;
		public int end;
		public Show(int start, int dual) {
			this.start = start;
			this.end = start + dual;
		}

		public int compareTo(Show o) {
			return this.end - o.end;
		}
	}
}
