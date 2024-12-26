package com.redjujubetree.example.huawei;

public class WorkReward2 {
	static Input input;
	static {
		input = new Input("40 3\n" +
				"20 10\n" +
				"20 20\n" +
				"20 5");
	}

	public static void main(String[] args) {
		String[] sts = input.nextLine().split(" ");
		int T = Integer.parseInt(sts[0]);
		int n = Integer.parseInt(sts[1]);

		// 做 i  份工作， 在 T 小时内可以获得的最大收益
		int[][] income = new int[n+1][T+1] ;

		for (int i = 1; i <= n; i++) {
			Job job = new Job(input.nextLine().split(" "));
			for (int j = 1; j <= T; j++) {
				if (j < job.t) {
					// 如果时间不足以完成该项工作，则此时的最大收益为 再吃 j 时间内完成 i - 1 件工作锁获取的收益
					income[i][j] = income[i - 1][j];
				} else {
					// 如果时间足够完成该项工作， 则最大收益为：做这个工作和不作这个工作之间 收益的最大值
					int doIt = income[i - 1][j - job.t] + job.w;
					int notDo = income[i - 1][j];
					income[i][j] = Math.max(doIt, notDo);
				}
			}
		}

		System.out.println(income[n][T]);
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
