package com.redjujubetree.example.algorithm;

import java.util.ArrayList;

public class EmptyStackEnter {
	static Input input ;
	static {
		input = new Input("10 20 50 80 1 1");
	}
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<>();

		String[] ss = input.nextLine().split(" ");
		for (int i = 0; i < ss.length; i++) {
			int a = Integer.parseInt(ss[i]);
			if (list.isEmpty()) {
				list.add(a);
			} else {
				int sum = 0;
				for (int j = list.size() - 1; j >= 0; j--) {
					sum += list.get(j);
					if (sum == a) {
						while (list.size()>j) {
							list.remove(list.size()-1);
						}
						list.add(2 * a);
						break;
					} else if (sum > a) {
						list.add(a);
						break;
					}
				}
				if (sum < a) {
					list.add(a);
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		for (int i = list.size() - 1; i >= 0 ; i--) {
			builder.append(list.get(i)).append(" ");
		}
		System.out.println(builder.toString().trim());


	}




}
