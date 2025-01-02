package com.redjujubetree.example.algorithm;

import java.util.*;

public class ComputerVirus {

	static Input input;
	static {
		input = new Input("4\n" +
				"3\n" +
				"2 1 1\n" +
				"2 3 1\n" +
				"3 4 1\n" +
				"2");
	}

	static boolean[] cs;
	static int[] distance;
	static int[] parent;
	static Map<Integer, List<Path>> map;
	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine())+1;
		distance = new int[n];
		Arrays.fill(distance, Integer.MAX_VALUE);
		parent = new int[n];
		Arrays.fill(parent, -1);
		cs = new boolean[n];
		int pathes = Integer.parseInt(input.nextLine()) ;

		map = new HashMap<>();
		for (int i = 0; i < pathes; i++) {
			String[] ss = input.nextLine().split(" ");
			int  from = Integer.parseInt(ss[0]);
			int  to = Integer.parseInt(ss[1]);
			int time = Integer.parseInt(ss[2]);
			List<Path> orDefault = map.getOrDefault(from, new ArrayList<>());
			orDefault.add(new Path(from, to, time));
			map.put(from,orDefault);
		}

		int start = Integer.parseInt(input.nextLine());
		cs[start] = true;
		distance[start] = 0;
		dfs(map.get(start));

		boolean flag = true;
		int max = 0;
		for (int i = 1; i < cs.length; i++) {
			if (!cs[i]) {flag = false;}
			max = Math.max(max, distance[i]);
		}
		if (!flag) {
			System.out.println(-1);
		} else {
			System.out.println(max);
		}
	}

	public static void dfs(List<Path> path) {
		if (path == null || path.size() == 0) {
			return;
		}
		for (int i = 0; i < path.size(); i++) {
			Path p = path.get(i);
			int dis = distance[p.i] + p.t;
			if (cs[p.j]) {
				if (dis < distance[p.i]) {
					distance[p.i] = dis;
					parent[p.j] = p.i;
				}
			} else {
				cs[p.j] = true;
				distance[p.j] = dis;
				parent[p.j] = p.i;
			}
			dfs(map.get(p.j));
		}
	}

	static class Path{
		public int i;
		public int j;
		public int t;
		public Path(int i, int j, int t){
			this.i = i;
			this.j = j;
			this.t = t;
		}
	}
}
