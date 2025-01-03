package com.redjujubetree.huawei;

import java.util.ArrayList;
import java.util.List;

public class Virus {

	static Input input;
	static {
		input = new Input("1,0,1,0,0,0,1,0,1");
		//input = new Input("1,1,1,1,1,1,1,1,1");
		//input = new Input("0,0,0,0,0,0,0,0,0");
	}

	static int N;
	static int[][] arr;
	public static void main(String[] args) {
		String[] s = input.nextLine().split(",");
		int n = (int)Math.sqrt(s.length);
		N = n;
		arr = new int[n][n];
		int index = 0;
		List<Point> list = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int i1 = Integer.parseInt(s[index++]);
				if (1 == i1) {
					list.add(new Point(i, j));
				}
				arr[i][j] = i1;
			}
		}
		if (list.size() == 0 || list.size() == s.length){
			System.out.println(-1);
		} else {
			System.out.println(bfs(arr, list));
		}
	}

	public static int bfs(int arr[][], List<Point> list) {
		int result = 0;

		while (!list.isEmpty()) {
			result++;
			int size = list.size();
			List<Point> temp = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				Point point = list.get(i);
				if (isCleanArea(point.x - 1, point.y)) {
					temp.add(new Point(point.x - 1, point.y));
					arr[point.x - 1][point.y] = 1;
				}
				if (isCleanArea(point.x + 1, point.y)) {
					temp.add(new Point(point.x + 1, point.y));
					arr[point.x + 1][point.y] = 1;
				}
				if (isCleanArea(point.x, point.y - 1)) {
					temp.add(new Point(point.x, point.y - 1));
					arr[point.x][point.y - 1] = 1;
				}
				if (isCleanArea(point.x, point.y + 1)) {
					temp.add(new Point(point.x, point.y + 1));
					arr[point.x][point.y + 1] = 1;
				}
			}
			list = temp;
		}
		return result - 1;
	}

	public static boolean isCleanArea(int x, int y) {
		if (x < 0) return false;
		if (y < 0) return false;
		if (x >= N) return false;
		if (y >= N) return false;
		return arr[x][y] == 0;
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
