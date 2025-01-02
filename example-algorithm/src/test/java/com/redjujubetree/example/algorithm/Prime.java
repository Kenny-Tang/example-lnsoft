package com.redjujubetree.example.algorithm;

public class Prime {
	static Input input;
	static {
		input = new Input("27");
		input = new Input("4");
	}

	public static void main(String[] args) {
		Integer num = Integer.valueOf(input.nextLine());

		int[] x = factorialToPrime(num);
		System.out.println(x[0] + " " + x[1]);
	}

	public static int[] factorialToPrime(int n) {
		for (int i = 2; i * i <= n; i++) {
			if (n % i == 0) {
				int other = n / i;
				if (isPrime(i) && isPrime(other)) {
					return new int[]{i, other};
				}
			}
		}
		return new int[]{-1, -1};
	}

	public static boolean isPrime(int n) {
		if (n < 2) {
			return false;
		}
		for (int i = 2; i*i <= n; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
}
