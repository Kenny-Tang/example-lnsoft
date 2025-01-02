package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.List;

public class JumpHourse {
	static Input input;
	static {
		input = new Input("3 2\n" +
				". .\n" +
				"2 .\n" +
				". .");
		input = new Input("3 5\n" +
				"4 7 . 4 8\n" +
				"4 7 4 4 .\n" +
				"7 . . . .");
	}
	static int[][] arr;
	static int[][] go = {{1, 2}, {1, -2}, {2, 1}, {2, -1}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}};

	public static void main(String[] args) {
		String[] ss = input.nextLine().split(" ");
		int m = Integer.parseInt(ss[0]);
		int n = Integer.parseInt(ss[1]);
		arr = new int[m][n];
		int hourseNum = 0;
		for (int i = 0; i < m; i++) {
			String[] s = input.nextLine().split(" ");
			for (int j = 0; j < n; j++) {
				if (!".".equals(s[j])) {
					arr[i][j] = Integer.parseInt(s[j]);
					hourseNum++;
				} else {
					arr[i][j] = -1;
				}
			}
		}
		int[][] step = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				List<Point> points = new ArrayList<>();
				points.add(new Point(i, j));
				int x = bfs(points,new int[m][n], hourseNum);
				step[i][j] = x;
			}
		}
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				min = Math.min(min, step[i][j]);
			}
		}
		if (min == Integer.MAX_VALUE) {
			System.out.println(-1);
		} else {
			System.out.println(min);
		}
	}

	public static int bfs(List<Point> points, int[][] visted, int hourseNum) {
		int step = 0;
		int m = visted.length;
		int n = visted[0].length;
		int result = 0;
		while (!points.isEmpty()) {
			if (step > 9){
				break;
			}
			if (hourseNum == 0) {
				break;
			}

			int size = points.size();
			List<Point> temp = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				Point point = points.get(i);
				if (arr[point.x][point.y] >= step && visted[point.x][point.y] == 0) {
					result += step;
					hourseNum--;
				}
				visted[point.x][point.y] = 1;
				for (int j = 0; j < go.length; j++) {
					int x = point.x + go[j][0];
					int y = point.y + go[j][1];
					if (x < 0 || y < 0 || x >= m || y >= n || visted[x][y] == 1) {
						continue;
					}
					temp.add(new Point(x, y));
				}
			}
			points = temp;
			step++;
		}
		if (hourseNum > 0) {
			return Integer.MAX_VALUE;
		}
		return result;
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
