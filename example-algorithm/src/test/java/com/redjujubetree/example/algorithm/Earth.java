package com.redjujubetree.example.algorithm;

import java.util.*;

public class Earth {
	static int N = 0;
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		N = scanner.nextInt();
		int startCount = scanner.nextInt();
		Map<Integer, List<Integer>> map = new HashMap<>();
		for (int i = 0; i < startCount; i++) {
			int key = scanner.nextInt();
			List<Integer> orDefault = map.getOrDefault(key, new ArrayList<>());
			int e = scanner.nextInt();
			orDefault.add(e);
			map.put(key, orDefault);
		}
		boolean[] visited = new boolean[N];
		List<Integer> nextStartupList = map.get(0);
		int key = 0;
		while(nextStartupList.size() > 0) {
			/*
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < nextStartupList.size(); i++) {
				sb.append(nextStartupList.get(i) + " ");
			}
			System.out.println("当前启动" + sb.toString());
			*/
			// 瑕疵启动
			List<Integer> next = map.getOrDefault(key+1, new ArrayList<>());
			for (int i = 0; i < nextStartupList.size(); i++) {
				Integer i1 = nextStartupList.get(i);
				visited[i1] = true;
				int pre = ( i1 - 1 + N) % N ;
				int after = (i1 + 1) % N;
				// 如果未启动，加入下次启动
				if (!visited[pre] && !next.contains(pre)) {
					next.add(pre);
				}
				if (!visited[after] && !next.contains(after)) {
					next.add(after);
				}
			}
			if (next.size() == 0) {
				break;
			}
			key++;
			nextStartupList = next;
		}
		Collections.sort(nextStartupList);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < nextStartupList.size(); i++) {
			builder.append(nextStartupList.get(i) + " ");
		}
		System.out.println(key);
		System.out.println(builder.toString().trim());
	}
}


