package com.redjujubetree.example;

import com.redjujubetree.example.huawei.Input;

import java.util.*;
class Point {
int x, y;
Point(int x, int y) {
this.x = x;
this.y = y;
}
}
public class Main {
// 排序比较函数
private static Comparator<Point> pointComparator = (p1, p2) -> {
if (p1.x != p2.x) {
return Integer.compare(p1.x, p2.x); // 按横坐标升序排列
}
return Integer.compare(p1.y, p2.y); // 如果横坐标相等，按纵坐标升序排列
};
// 计算两点之间的距离的平方（避免浮点误差）
private static int twoPointDistanceSquared(Point p1, Point p2) {
return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
}
// 判断三点是否构成直角（通过向量内积判断）
private static boolean isRightAngle(Point a, Point b, Point c) {
	int dotProduct = (a.x - b.x) * (a.x - c.x) + (a.y - b.y) * (a.y - c.y);
	return dotProduct == 0; // 内积为0表示直角
}
// 判断四个点是否构成正方形
private static boolean isSquare(Point[] points) {
// 对四个点进行排序
Arrays.sort(points, pointComparator);
// 计算四条边的平方长度
int s1 = twoPointDistanceSquared(points[0], points[1]);
int s2 = twoPointDistanceSquared(points[1], points[3]);
int s3 = twoPointDistanceSquared(points[3], points[2]);
int s4 = twoPointDistanceSquared(points[2], points[0]);
// 判断四条边是否相等，且存在一个直角
return (s1 == s2 && s2 == s3 && s3 == s4 && s1 != 0 && isRightAngle(points[0], points[1], points[3]));
}
// 计算能够构成的正方形的数量
public static int countSquares(List<Point> points) {
int squareCount = 0;
int N = points.size();
// 枚举所有四个点的组合
for (int i = 0; i < N; ++i) {
for (int j = i + 1; j < N; ++j) {
for (int k = j + 1; k < N; ++k) {
for (int l = k + 1; l < N; ++l) {
Point[] quad = { points.get(i), points.get(j), points.get(k), points.get(l) };
if (isSquare(quad)) {
squareCount++;
}
}
}
}
}
return squareCount;
}
public static void main(String[] args) {
int N = Integer.parseInt(input.nextLine());
List<Point> points = new ArrayList<>();
// 输入N个点的坐标
for (int i = 0; i < N; ++i) {
	String[] s = input.nextLine().split(" ");
int x = Integer.parseInt(s[0]);
int y = Integer.parseInt(s[1]);
points.add(new Point(x, y));
}
// 计算可以构成的正方形数量
System.out.println(countSquares(points));
}
	static Input input;
	static  {
		input = new Input("4\n" +
				"0 0\n" +
				"1 2\n" +
				"3 1\n" +
				"2 -1");
	}
}