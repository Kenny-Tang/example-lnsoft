package com.redjujubetree.huawei;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WeAreATeam {
	static Input input ;
	static {
		input = new Input("5 6\n" +
				"1 2 0\n" +
				"1 2 1\n" +
				"1 5 0\n" +
				"2 3 1\n" +
				"2 5 1\n" +
				"1 3 2");
	}

	public static void main(String[] args) {
		Map<Integer, Set<Integer>> map = new HashMap<>();
		String[] s = input.nextLine().split(" ");
		int n = Integer.parseInt(s[0]) ;
		int m = Integer.parseInt(s[1]) ;

		for (int i = 0; i < m; i++) {
			String[] split = input.nextLine().split(" ");
			int a = Integer.parseInt(split[0]);
			int b = Integer.parseInt(split[1]);
			int c = Integer.parseInt(split[2]);
			if (a < 0 || a > n || b< 0 || b > n || c < 0 || c > 1) {
				System.out.println("da pian zi");
			} else {
				if (c == 0) {
					Set<Integer> orDefault = map.getOrDefault(a, new HashSet<>());
					orDefault.add(a);
					orDefault.add(b);
					map.put(a, orDefault);
					map.put(b, orDefault);
				} else if (c == 1) {
					Set<Integer> orDefault = map.getOrDefault(a, new HashSet<>());
					if (orDefault.contains(b)) {
						System.out.println("we are a team");
					} else {
						System.out.println("we are not a team");
					}
				}
			}
		}
	}
}
