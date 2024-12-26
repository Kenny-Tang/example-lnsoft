package com.redjujubetree.example.huawei;

public class Secret {

	static Input input;
	static {
		input = new Input("0 9 20 -1 -1 15 7 -1 -1 -1 -1 3 2\n");
	}

	public static void main(String[] args) {
		String[] strings = input.nextLine().split(" ");
		int[] nodes = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			nodes[i] = Integer.parseInt(strings[i]);
		}

		int time = getTime(nodes, 0);

		System.out.println(time);
	}

	public static int getTime(int[] nodes, int index) {
		if (index >= nodes.length) {
			return 0;
		}
		int left = getTime(nodes, index * 2 + 1);
		int right = getTime(nodes, index * 2 + 2);
		int max = Math.max(left, right);
		if (index == 0) {
			return max;
		}
		return nodes[index] + max;
	}
}
