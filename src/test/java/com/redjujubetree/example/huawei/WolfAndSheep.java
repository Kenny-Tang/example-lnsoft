package com.redjujubetree.example.huawei;

public class WolfAndSheep {
	static Input input;
	static {
		input = new Input("5 3 3");
		input = new Input("5 4 1");
	}
	static int ret  = Integer.MAX_VALUE;
	public static void main(String[] args) {
		String[] split = input.nextLine().split(" ");
		int sheep = Integer.parseInt(split[0]);
		int wolf = Integer.parseInt(split[1]);
		int boat = Integer.parseInt(split[2]);

		dfs(wolf, sheep,0,0, boat, 0);

		System.out.println(ret == Integer.MAX_VALUE ? 0 : ret);

	}
	public static void dfs(int wolf, int sheep,int oppo_wolf, int oppo_sheep, int boat, int count) {
		if (sheep == 0 && wolf == 0) {
			ret = Math.min(ret, count);
			return;
		}

		if (sheep + wolf <= boat) {
			ret = Math.min(ret, count+1);
			return;
		}

		for (int i = 0; i <= boat && i <= wolf; i++) {
			for (int j = 0; j <= boat - i && j <= sheep; j++) {
				// 当前不符合
				if (wolf >= sheep) { continue;}
				// 运送苏亮不符合
				if (j + j == 0) {continue;}
				int o_w = oppo_wolf + i ;
				int o_sh = sheep + j ;
				if (o_w >= o_sh) {continue;}

				dfs(wolf-i, sheep - j, o_w, o_sh, boat, count+1);
			}
		}
	}
}
