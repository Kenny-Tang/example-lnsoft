package com.redjujubetree.example.algorithm;

public class ManQueueLen {

	static Input input;
	static  {
		input = new Input("3,4\n" +
				"F,M,M,F\n" +
				"F,M,M,F\n" +
				"F,F,F,M");
	}
	static  int X;
	static int Y;
	static String[][] matrix;
	public static void main(String[] args) {
		String[] ss = input.nextLine().split(",");
		X = Integer.parseInt(ss[0]);
		Y = Integer.parseInt(ss[1]);
		matrix = new String[X][Y];
		for (int i = 0; i < X; i++) {
			String[] ms = input.nextLine().split(",");
			for (int j = 0; j < Y; j++) {
				matrix[i][j] = ms[j];
			}
		}
		int maxLen = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				maxLen = Math.max(getMaxLen(i, j), maxLen);
			}
		}

		System.out.println(maxLen);

	}
	public static int getMaxLen(int x, int y) {
		int maxLen = 0;
		maxLen = Math.max(maxLen, getMaxLen(x, y, 0));
		maxLen = Math.max(maxLen, getMaxLen(x, y, 1));
		maxLen = Math.max(maxLen, getMaxLen(x, y, 2));
		maxLen = Math.max(maxLen, getMaxLen(x, y, 3));
		return maxLen;
	}

	// 0 正方向
	// 1 下方
	// 2 右下方向
	// 3 左下方向
	static int[][] directions = {{0, 1}, {1, 1}, {1, 0}, {1, -1}};
	public static int getMaxLen(int x, int y, int direction) {
		if (x < 0 || x >= X || y < 0 || y >= Y) {
			return 0;
		}
		if (matrix[x][y].equals("F")) {
			return 0;
		}
		int[] dir = directions[direction];
		return getMaxLen(x + dir[0], y + dir[1], direction) + 1;
	}
}