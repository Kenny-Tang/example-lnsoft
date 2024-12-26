package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.HashSet;

public class Square {
	static Input input;
	static  {
		input = new Input("4\n" +
				"0 0\n" +
				"1 2\n" +
				"3 1\n" +
				"2 -1");
	}

	public static void main(String[] args) {
		Integer count = Integer.parseInt(input.nextLine());
		ArrayList<Point> points = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			points.add(new Point(input.nextLine().split(" ")));
		}

		int counter = 0;
		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				for (int k = j + 1; k < count; k++) {
					for (int m = k + 1; m < count; m++) {
						if (isSquare(points.get(i), points.get(j), points.get(k), points.get(m))) {
							counter++;
						}
					}
				}
			}
		}
		System.out.println(counter);
	}

	/**
	 * 先求出任意两个点的距离，一共六个距离。
	 *
	 * 如果这六个距离，没有0并且只有两种距离，那么就是正方形。
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return
	 */
	public static boolean isSquare(Point p1, Point p2, Point p3, Point p4) {
		Point[] points = {p1, p2, p3, p4};
		int d1 = distance(p1, p2);
		int d2 = distance(p2, p3);
		int d3 = distance(p3, p4);
		int d4 = distance(p4, p1);
		int d5 = distance(p1, p3);
		int d6 = distance(p2, p4);
		HashSet<Integer> set = new HashSet<>();
		set.add(d1);
		set.add(d2);
		set.add(d3);
		set.add(d4);
		set.add(d5);
		set.add(d6);
		return set.size() == 2 && d1>0 && d2 > 0 && d3 > 0 && d4 > 0 && d5 > 0 && d6 > 0;
	}

	public static int distance(Point p1, Point p2) {
		int x = p1.x - p2.x;
		int y = p1.y - p2.y;
		return x*x + y*y;
	}

	static class Point {
		public int x;
		public int y;
		public Point(String[] xy) {
			this.x = Integer.parseInt(xy[0]);
			this.y = Integer.parseInt(xy[1]);
		}

	}
}
