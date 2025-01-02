package com.redjujubetree.example.algorithm;

public class BestPath {
	static Input input ;
	static {
		input = new Input("3\n" +
				"3\n" +
				"5 4 5\n" +
				"1 2 6\n" +
				"7 4 6");
		input = new Input("6\n" +
				"5\n" +
				"3 4 6 3 4\n" +
				"0 2 1 1 7\n" +
				"8 8 3 2 7\n" +
				"3 2 4 9 8\n" +
				"4 1 2 0 0\n" +
				"4 6 5 4 3");
	}
	static int max = 0;
	static int m;
	static int n;
	static int[][] matrix;

	public static void main(String[] args) {
		m = Integer.parseInt(input.nextLine());
		n = Integer.parseInt(input.nextLine());
		matrix = new int[m][n];
		for (int i = 0; i < m; i++) {
			String[] ss = input.nextLine().split(" ");
			for (int j = 0; j < n; j++) {
				matrix[i][j] = Integer.parseInt(ss[j]);
			}
		}
		max = dfs(0,0, Integer.MAX_VALUE);
		System.out.println(max);

	}
	static int[][] direction = {{0,1}, {0,-1}, {1,0}, {-1,0}} ;

	public static int dfs(int x, int y, int min) {
		if (x < 0 || x >= m || y< 0 || y >= n || matrix[x][y] == -1) {
			return Integer.MIN_VALUE;
		}
		if (x == m - 1 && y == n - 1) {
			return Math.min(matrix[x][y], min);
		}
		int resut = Integer.MIN_VALUE;
		for (int i = 0; i < direction.length; i++) {
			int newX = x + direction[i][0];
			int newY = y + direction[i][1];
			int temp = matrix[x][y];
			matrix[x][y] = -1;
			resut = Math.max(dfs(newX, newY, Math.min(temp, min)), resut);
			matrix[x][y] = temp;
		}
		return resut;
	}


}
