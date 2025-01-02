package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxTask {

	static Input input;

	static {
		input = new Input("3\n" +
				"1 1\n" +
				"1 2\n" +
				"1 3");
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < n; i++ ) {
			String[] ss = input.nextLine().split(" ");
			tasks.add(new Task(Integer.parseInt(ss[0]), Integer.parseInt(ss[1])));
		}
		Collections.sort(tasks);
		int max = 0;
		int time = 0;
		for (int i = 0; i < tasks.size(); i++) {
			// 任务开始前空闲一定可以做
			if (time <= tasks.get(i).start) {
				time = tasks.get(i).start + 1;
				max++;
			} else if (time <= tasks.get(i).end) {
				// 任务开始后 到结束时间有空闲， 该任务可以做
				time++;
				max++;
			}
		}
		System.out.println(max);
	}

	static class Task implements Comparable<Task>{
		int start;
		int end;
		public Task(int start, int end) {
			this.start = start;
			this.end = end;
		}
		public int compareTo(Task o ) {
			return this.end - o.end;
		}
	}
}
