package com.redjujubetree.example.algorithm;

public class RobotLiveArea {

	static Input input ;
	static {
		input = new Input("4 4\n" +
				"1 2 5 2\n" +
				"2 4 4 5\n" +
				"3 5 7 1\n" +
				"4 6 2 4");
	}
	static  int[][] area;
	static int m, n;
	static boolean[][] visited;

	public static void main(String[] args) {
		String[] str = input.nextLine().split(" ");

		m = Integer.parseInt(str[0]);
		n = Integer.parseInt(str[1]);
		area = new int[m][n];
		visited = new boolean[m][n];

		for (int i = 0; i < m; i++) {
			String[] ss = input.nextLine().split(" ");
			for (int j = 0; j < n; j++) {
				area[i][j] = Integer.parseInt(ss[j]);
			}
		}

		int result = 1;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result = Math.max(dfs(i, j), result);
			}
		}

		System.out.println(result);
	}

	public static int dfs(int x, int y) {
		if (isOverBound(x, y)) {
			return 0;
		}
		if (visited[x][y]) {
			return 0;
		}
		visited[x][y] = true;

		int result = 1;
		int[][] direction = new int[][]{{1,0},{-1,0},{0,1},{0,-1}};
		for (int i = 0; i < direction.length; i++) {
			int newX = x + direction[i][0];
			int newY = y + direction[i][1];
			if (!isOverBound(newX, newY) // 下一个坐标没有越界
					&& !visited[newX][newY]  // 下一个坐标没有被访问过
					&& Math.abs(area[newX][newY] - area[x][y]) <= 1) { // 下一个高度差不是过大
				result += dfs(newX, newY);
			}
		}
		return result;
	}

	public static boolean isOverBound(int x, int y) {
		if (x >= m || y >= n || x < 0 || y < 0) {
			return true;
		}
		return false;
	}

}
