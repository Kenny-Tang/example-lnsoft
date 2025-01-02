package com.redjujubetree.example.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PerfectTaskPlan {
	static Input input;
	static {
		input = new Input("2,2,2,3\n" +
				"2");
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String[] split = scanner.nextLine().split(",");
		int k = scanner.nextInt();

		int max = 1;
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < split.length; i++) {
			int key = Integer.parseInt(split[i]);
			max = Math.max(key, max);
			int value = map.getOrDefault(key, 0);
			map.put(key,value + 1);
		}

		int completed = 0;
		int timeSpent = 0;
		int[] waitList = new int[max+1];
		while (completed < split.length) {
			timeSpent++;
			for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
				if (entry.getValue() > 0 && waitList[entry.getKey()] < timeSpent) {
					completed++;
					waitList[entry.getKey()] = k + timeSpent;
					map.put(entry.getKey(), entry.getValue() - 1);
					break;
				}
			}
		}
		System.out.println(timeSpent);
	}

}
