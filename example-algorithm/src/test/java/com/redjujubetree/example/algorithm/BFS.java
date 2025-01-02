package com.redjujubetree.example.algorithm;

import java.util.ArrayDeque;

public class BFS {

	static Input input;
	static {
		input = new Input("4,4,0,0,3,3");
	}
	public static void main(String[] args) {
		String[] split = input.nextLine().split(",");
		int m = Integer.parseInt(split[0]);
		int n = Integer.parseInt(split[1]);
		boolean[][] visited = new boolean[m][n];
		ArrayDeque<Point> deque =  new ArrayDeque<>();
		Point point = new Point(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
		deque.add(point);
		visited[point.x][point.y] = true;
		Point point1 = new Point(Integer.parseInt(split[4]), Integer.parseInt(split[5]));
		deque.add(point1);
		visited[point1.x][point1.y] = true;

		int timecost = -1;
		while (!deque.isEmpty()) {
			int size = deque.size();
			timecost++;
			for (int i = 0; i < size; i++) {
				Point poll = deque.poll();
				visited[poll.x][poll.y] = true;
				if (poll.x - 1 >= 0 && !visited[poll.x - 1][poll.y]) {
					deque.add(new Point(poll.x - 1, poll.y));
				}
				if (poll.x + 1 < m && !visited[poll.x + 1][poll.y]) {
					deque.add(new Point(poll.x + 1, poll.y));
				}
				if (poll.y - 1 >= 0 && !visited[poll.x][poll.y - 1]) {
					deque.add(new Point(poll.x, poll.y - 1));
				}
				if (poll.y + 1 < n && !visited[poll.x][poll.y + 1]) {
					deque.add(new Point(poll.x, poll.y + 1));
				}
			}
		}
		System.out.println(timecost);
	}

	static class Point {
		public int x;
		public int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
