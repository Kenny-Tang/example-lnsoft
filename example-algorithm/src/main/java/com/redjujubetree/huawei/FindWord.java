package com.redjujubetree.huawei;

import java.util.ArrayList;
import java.util.List;

public class FindWord {
	static Input input;
	static {
		input = new Input("4\n" +
				"A,C,C,F\n" +
				"C,D,E,D\n" +
				"B,E,S,S\n" +
				"F,E,C,A\n" +
				"ACCESS");
	}

	public static boolean found = false;
	static String word = "";
	static List<Point> result = new ArrayList<>();
	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		char[][] chs = new char[n][n];
		for (int i = 0; i < n; i++) {
			String[] split = input.nextLine().split(",");
			for (int j = 0; j < n; j++) {
				chs[i][j] = split[j].charAt(0);
			}
		}
		word = input.nextLine();
		for (int i = 0; i < word.length(); i++) {
			for (int j = 0; j < word.length(); j++) {
				dfs(chs, new Point(i,j), 0);
			}
		}

	}
	static  int[][] direction = {{0,1}, {0,-1}, {1,0}, {-1,0}};
	static void dfs(char[][] chs, Point p, int count) {
		if (found) {
			return;
		}
		if (p.x < 0 || p.y < 0 || p.x >= chs.length || p.y >= chs[0].length) {
			return;
		}
		if (chs[p.x][p.y] == word.charAt(count)) {
			result.add(new Point(p.x,p.y));
			if (result.size() == word.length()) {
				found = true;
				List<String> list = new ArrayList<>();
				for (int i = 0; i < result.size(); i++) {
					Point point = result.get(i);
					list.add(point.x+"");
					list.add(point.y+"");
				}
				System.out.println(String.join(",", list));
			}
		} else {
			return;
		}

		for (int i = 0; i < direction.length; i++) {
			char ch = chs[p.x][p.y];
			chs[p.x][p.y] = '#';
			dfs(chs, new Point(p.x + direction[i][0], p.y + direction[i][1]), count+1);
			chs[p.x][p.y] = ch;
		}
	}

	static class Point{
		public int x;
		public int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
