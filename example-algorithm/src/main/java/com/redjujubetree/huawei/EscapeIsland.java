package com.redjujubetree.huawei;

import java.util.Stack;

public class EscapeIsland {

	static Input input ;
	static {
		input = new Input("5 10 8 -8 -5");
	}

	public static void main(String[] args) {
		Stack<Integer> right = new Stack<>();
		Stack<Integer> left = new Stack<>();
		String[] split = input.nextLine().split(" ");
		for (int i = 0; i < split.length; i++) {
			int val = Integer.parseInt(split[i]) ;
			if (val == 0) {
				System.out.println(-1);
				return;
			}
			if (val > 0) {
				right.push(val);
			} else {

				int power = val;

				while (!right.isEmpty() && power < 0) {
					int r = right.pop();
					power += r;
				}
				if (power > 0) {
					right.push(power);
				} else if(power < 0) {
					left.push(power);
				}
			}
		}
		System.out.println(right.size() + left.size());
	}

}