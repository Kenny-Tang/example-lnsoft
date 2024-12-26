package com.redjujubetree.example.huawei;

public class NextMaxNumber {
	static Input input;
	static {
		input = new Input("2615371\n" +
				"4");
	}

	public static void main(String[] args) {
		String num1 = input.nextLine();
		int remove = Integer.parseInt(input.nextLine());
		while (remove > 0) {
			if (num1.charAt(0) > num1.charAt(1)) {
				num1 = num1.substring(1);
			} else {
				if (num1.charAt(1) < num1.charAt(2)) {
					num1 = num1.substring(0,2) + num1.substring(3);
				} else {
					num1 = num1.substring(0,1) + num1.substring(2);
				}
			}
			remove -= 1;
		}
		System.out.println(num1);
	}

}
