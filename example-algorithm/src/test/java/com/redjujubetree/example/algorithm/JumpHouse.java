package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.Collections;

public class JumpHouse {
	static Input input;
	static {
		input = new Input("[1,4,5,2,2]\n" +
				"7");
		input = new Input("[-1,2,4,9,6]\n" +
				"8");
	}

	public static void main(String[] args) {
		String[] split = input.nextLine().replace("[", "").replace("]", "").split(",");
		int[] steps = new int[split.length];


		for (int i = 0; i < split.length; i++) {
			steps[i]= Integer.parseInt(split[i].trim());
		}

		int count = Integer.parseInt(input.nextLine());

		ArrayList<Combine> list = new ArrayList<>() ;
		for (int i = 0; i < steps.length; i++) {
			for (int j = i+1; j < steps.length; j++) {
				if (steps[i] + steps[j] == count) {
					list.add(new Combine(i, j));
					break;
				}
			}
		}
		Collections.sort(list);
		if (!list.isEmpty()) {
			Combine combine = list.get(0);
			System.out.println( "[" + steps[combine.index1] + ", " + steps[combine.index2]+"]");
		}
	}

	static class Combine implements Comparable<Combine>{
		public int index1;
		public int index2;
		public int sumIndex;

		public Combine(int index1, int index2) {
			this.index1 = index1;
			this.index2 = index2;
			this.sumIndex = index1 + index2;
		}
		public int compareTo(Combine o) {
			return sumIndex - o.sumIndex;
		}
		public String toString() {
			return "[" + index1 + ", " + index2+"]";
		}
	}
}
