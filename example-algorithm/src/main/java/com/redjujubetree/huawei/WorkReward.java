package com.redjujubetree.huawei;

public class WorkReward {
	static Input input;
	static {
		input = new Input("40 3\n" +
				"20 10\n" +
				"20 20\n" +
				"20 5");
	}

	public static void main(String[] args) {
		String str = input.nextLine();
		int T = Integer.parseInt(str.split(" ")[0]);
		int n = Integer.parseInt(str.split(" ")[1]);

		int[] income = new int[T+1];

		for (int i = 0; i < n; i++) {
			Job job = new Job(input.nextLine().split(" "));
			for (int j = T; j >= job.t; j-- ){
				// 选择这份工作 总收益 = 干这份工作取得的收益  job.w + 减去这份工作需要时间后剩余的时间 可以获得的最大的收益 incone[j - job.t]
				// 不选择这份工作
				income[j] = Math.max(income[j], income[j - job.t] + job.w);
			}
		}
		System.out.println(income[T]);
	}

	static class Job{
		public int t ;
		public int w;
		public Job(String[] s) {
			this.t = Integer.parseInt(s[0]);
			this.w = Integer.parseInt(s[1]);
		}
	}
}
