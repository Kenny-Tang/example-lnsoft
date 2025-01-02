package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CombineNumber {
	static Input input;
	static {
		input = new Input("1,4,8,7");
		input = new Input("3,9,7,8");
	}

	public static void main(String[] args) {
		String[] ss = input.nextLine().split(",");
		List<Integer> nums = new ArrayList<>();
		int N = -1;

		if (ss.length != 4){
			System.out.println(-1);
			return;
		}

		for (int i = 0; i < ss.length; i++) {
			int e = Integer.parseInt(ss[i]);
			if (e < 1 || e > 9) {
				System.out.println(-1);
				return;
			}
			N = Math.max(N, e);
			nums.add(e);
			if (e == 2) {
				if (nums.contains(5)) {
					System.out.println(-1);
					return;
				}
				nums.add(5);
			}
			if (e == 5) {
				if (nums.contains(2)) {
					System.out.println(-1);
					return;
				}
				nums.add(2);
			}
			if (e == 6) {
				if (nums.contains(9)) {
					System.out.println(-1);
					return;
				}
				nums.add(9);
			}
			if (e == 9) {
				if (nums.contains(6)) {
					System.out.println(-1);
					return;
				}
				nums.add(6);
			}
		}

		int size = nums.size();
		List<Integer> resultList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			Integer e = nums.get(i);
			resultList.add(e);
		}

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i == j) continue;
				if (resultList.get(i) == 2 && resultList.get(j) == 5) {
					continue;
				}
				if (resultList.get(i) == 5 && resultList.get(j) == 2) {
					continue;
				}
				if (resultList.get(i) == 6 && resultList.get(j) == 9) {
					continue;
				}
				if (resultList.get(i) == 9 && resultList.get(j) == 6) {
					continue;
				}
				int x = resultList.get(i) * 10 + resultList.get(j);
				if (resultList.contains(x)){
					continue;
				}
				resultList.add(x);
			}
		}
		Collections.sort(resultList);
		System.out.println(resultList.get(N-1));

	}
}
