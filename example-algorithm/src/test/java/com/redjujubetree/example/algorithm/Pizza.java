package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
	static Input input;
	static {
		input = new Input("5\n" +
				"8\n" +
				"2\n" +
				"10\n" +
				"5\n" +
				"7");
	}

	public static void main(String[] args) {
		Integer n = Integer.parseInt(input.nextLine());
		List<Integer> pizzaes = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			pizzaes.add(Integer.parseInt(input.nextLine()));
		}
		int result = 0;
		for (int i = 0; i < n; i++) {
			List<Integer> dp = new ArrayList<>(n);
			for (int j = 0; j < n; j++) {
				dp.add(pizzaes.get((j+i) % n));
			}
			result = Math.max(result, getMax(dp, true));
		}
		System.out.println(result);
	}

	public static int getMax(List<Integer> piz, boolean head) {
		// 如果只有一个元素直接返回
		if (piz.size() == 1){
			return piz.get(0);
		}
		int result = head ? piz.get(0) : piz.get(piz.size() - 1);
		int tailMax = 0;
		int headMax = 0;
		if (head) {
			List<Integer> pizzaes = copy(piz);
			pizzaes.remove(0);
			if (pizzaes.get(0) >  pizzaes.get(pizzaes.size() - 1)) {
				pizzaes.remove(0);
			} else {
				pizzaes.remove(pizzaes.size() - 1);
			}
			headMax = Math.max(getMax(pizzaes, true),getMax(pizzaes, false));
		} else {
			List<Integer> pizzaes = copy(piz);
			pizzaes.remove(pizzaes.size() - 1);
			if (pizzaes.get(0) >  pizzaes.get(pizzaes.size() - 1)) {
				pizzaes.remove(0);
			} else {
				pizzaes.remove(pizzaes.size() - 1);
			}
			tailMax = Math.max(getMax(pizzaes, true),getMax(pizzaes, false));
		}
		result += Math.max(headMax, tailMax);

		return result;
	}
	public static List<Integer> copy(List<Integer> pizzaes) {
		List<Integer> result = new ArrayList<>(pizzaes.size());
		for (int i = 0; i < pizzaes.size(); i++) {
			result.add(pizzaes.get(i));
		}
		return result;
	}
}
