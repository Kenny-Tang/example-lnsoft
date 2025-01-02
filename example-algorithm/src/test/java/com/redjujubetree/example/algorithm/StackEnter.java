package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.List;

public class StackEnter {

	static List<Integer> list = new ArrayList<>();

	static Input input;
	static {
		input = new Input("5 10 20 50 85 1");
		input = new Input("6 7 8 13 9");
	}

	public static void main(String[] args) {
		String[] split = input.nextLine().split(" ");
		for (int i = 0; i < split.length; i++) {
			push(Integer.parseInt(split[i]));
		}
		Integer pop = pop();
		StringBuilder builder = new StringBuilder();
		while (pop != null) {
			builder.append(pop).append(" ");
			pop = pop();
		}
		System.out.println(builder.toString().trim());
	}

	public static void push(Integer value){
		int sum = 0;
		for (int i = list.size() - 1; i >= 0; i--) {
			sum += list.get(i);
			if (sum == value) {
				break;
			} else if (sum > value) {
				break;
			}
		}
		if (sum == value) {
			while (sum > 0) {
				Integer pop = pop();
				sum -= pop;
			}
			value = value * 2;
		}
		list.add(value);
	}

	public static Integer pop(){
		if (list.isEmpty()){
			return null;
		}
		return list.remove(list.size()-1);
	}

}
