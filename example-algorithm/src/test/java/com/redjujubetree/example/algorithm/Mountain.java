package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Mountain {
	static Input input;
	static {
		input = new Input("5 4 1\n" +
				"0 1 2 0\n" +
				"1 0 0 0\n" +
				"1 0 1 2\n" +
				"1 3 1 0\n" +
				"0 0 0 9");
	}
	static int maxHight = 0;
	static List<Point> list = new ArrayList<>();
	static boolean[][] visited;
	static int m = 0;
	static int n = 0;
	static int k = 0;
	static int[][] matrix;
	public static void main(String[] args) {
		String[] ss = input.nextLine().split(" ");

		m = Integer.parseInt(ss[0]);
		n = Integer.parseInt(ss[1]);
		k = Integer.parseInt(ss[2]);

		matrix = new int[m][n];
		for (int i = 0; i < m; i++) {
			String[] split = input.nextLine().split(" ");
			for (int j = 0; j < n; j++) {
				matrix[i][j] = Integer.parseInt(split[j]);
			}
		}

		visited = new boolean[m][n];
		list.add(new Point(0, 0));
		int step = bfs();
		System.out.print(step + " " );
		System.out.println(maxHight);
	}

	static int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
	public static int bfs() {
		int result = 0;
		int steps = 0;
		while (!list.isEmpty()) {
			List<Point> temp = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				Point point = list.get(i);
				if (visited[point.x][point.y] || point.x < 0 || point.y < 0 || point.x >= m || point.y >= n) {
					continue;
				}
				visited[point.x][point.y] = true;
				if ( matrix[point.x][point.y] > maxHight) {
					maxHight = matrix[point.x][point.y];
					result = steps;
				}
				for (int[] d : direction) {
					int x = point.x + d[0];
					int y = point.y + d[1];
					if (x < 0 || x >= m || y < 0 || y >= n || visited[x][y]) {
						continue;
					}
					if (Math.abs(matrix[point.x][point.y] - matrix[x][y]) <= k) {
						temp.add(new Point(x, y));
					}
				}
			}
			list = temp;
			steps++;
		}
		return result ;
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
