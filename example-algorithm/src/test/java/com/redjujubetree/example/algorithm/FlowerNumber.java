package com.redjujubetree.example.algorithm;

public class FlowerNumber {

	static Input input;
	static {
		input = new Input("3\n" +
				"0");
	}

	static int n;
	public static void main(String[] args) {
		n = Integer.parseInt(input.nextLine());
	}

	static boolean isFlowerNum(int number) {
		int result = 0;
		while (number > 0) {
			result += powerN(number);
			number /= 10;
		}
		return result == n;
	}
	static int powerN(int number) {
		return (int) Math.pow(number, n);
	}

}
