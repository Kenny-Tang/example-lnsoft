package com.redjujubetree.huawei;

public class Linux {
	static Input input;
	static  {
		input = new Input("4\n" +
				"1 1 0 0\n" +
				"1 1 1 0\n" +
				"0 1 1 0\n" +
				"0 0 0 1");
	}
	static int total;
	static int[][] matrix;
	public static void main(String[] args) {
		total = Integer.parseInt(input.nextLine());
		matrix = new int[total][total];

		for (int i = 0; i < total; i++) {
			String[] ss = input.nextLine().split(" ");
			for (int j = 0; j < total; j++) {
				matrix[i][j] = Integer.parseInt(ss[j]);
			}
		}

		int max = 0;
		for (int i = 0; i < total; i++) {
			int bfs = dfs(i);
			max = Math.max(bfs, max);
		}
		System.out.println(max);
	}

	public static int dfs(int row) {
		int result = 1;
		for (int i = row + 1; i < total; i++ ) {
			if (matrix[row][i] == 1) {
				result += dfs(i);
			}
		}
		return result;
	}

}
