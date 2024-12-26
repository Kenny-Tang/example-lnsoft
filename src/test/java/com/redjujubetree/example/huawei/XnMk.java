package com.redjujubetree.example.huawei;

public class XnMk {
	static Input input;
	static {
		input = new Input("6 7\n" +
				"2 12 6 3 5 5\n" +
				"10 11\n" +
				"1 1 1 1 1 1 1 1 1 1");
	}

	public static void main(String[] args) {
		while (input.hasNextLine()) {
			String[] ss = input.nextLine().split(" ");
			String[] cards = input.nextLine().split(" ");
			int n = Integer.parseInt(ss[0]);
			int target = Integer.parseInt(ss[1]);

			int sum = 0;

			int[] intCards = new int[n+1];
			for (int i = 1; i <= n; i++) {
				sum += Integer.parseInt(cards[i-1]);
				intCards[i] = sum;
			}

			boolean found = false;
			outer:
			for (int i = 1; i <= n; i++) {
				for (int j = i+1; j <= n; j++) {
					if ((intCards[j] - intCards[i] ) % target == 0) {
						System.out.println(1);
						found = true;
						break outer;
					}
				}
			}
			if (!found) {
				System.out.println(0);
			}

		}
	}
}













