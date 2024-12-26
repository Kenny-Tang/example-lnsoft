package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.Collections;

public class MinArraySum {
	static Input input ;
	static  {
		input = new Input("3 1 1 2\n" +
				"3 1 2 3\n" +
				"2");
	}

	public static void main(String[] args) {
		String[] s1 = input.nextLine().split(" ") ;
		String[] s2 = input.nextLine().split(" ") ;
		Integer k = Integer.valueOf(input.nextLine()) ;
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < s1.length; i++) {
			for (int j = 0; j < s2.length; j++) {
				list.add(Integer.parseInt(s1[i]) + Integer.parseInt(s2[j]));
			}
		}
		Collections.sort(list);
		int sum = 0;
		for (int i = 0; i < k; i++) {
			sum += list.get(i);
		}
		System.out.println(sum);
	}
}
