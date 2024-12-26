package com.redjujubetree.example.huawei;

import java.util.Stack;

public class Decode {
	static Input input;
	static {
		input = new Input("3[m2[c]]");
		input = new Input("3[k]2[mn]");
	}

	public static void main(String[] args) {
		String string = input.nextLine();
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == ']') {
				String str = "";
				while (stack.peek() != '[') {
					Character pop = stack.pop();
					str = pop + str ;
				}
				stack.pop(); // 弹出 [
				int base = 1;
				int repeat = 0;
				while (!stack.isEmpty() && Character.isDigit(stack.peek())) {
					Character pop = stack.pop();
					repeat += (pop - '0') * base;
					base *= 10;
				}
				String push = "";
				for (int ii = 0 ; ii < repeat; ii++) {
					push += str;
				}
				for (int j = 0; j < push.length(); j++) {
					stack.push(push.charAt(j));
				}
			} else {
				stack.push(string.charAt(i));
			}
		}
		StringBuffer sb = new StringBuffer();
		while (!stack.isEmpty()){
			sb.append(stack.pop());
		}
		System.out.println(sb.reverse().toString());
	}

}
