package com.redjujubetree.huawei;

import java.util.Stack;

public class LISP {

	static Input input;
	static {
		input = new Input("(div 12 (sub 45 45))");
		input = new Input("(add 1 (div -7 3))");
	}

	public static void main(String[] args) {
		String s = input.nextLine();

		try{
			int start = 0;
			Stack<String> stack = new Stack<>();
			for (int i = start; i < s.length(); i++) {
				if ('(' == s.charAt(i)) {
					start++;
					continue;
				}
				if (' ' == s.charAt(i)) {
					stack.push(s.substring(start, i));
					start = i + 1;
					continue;
				}
				if (')' == s.charAt(i)) {
					if (Character.isDigit(s.charAt(i-1))) {
						String num2 = s.substring(start, i);
						String num1 = stack.pop();
						String op = stack.pop();
						stack.push(""+cal(op, num1, num2));
					} else {
						String num2 = stack.pop();
						String num1 = stack.pop();
						String op = stack.pop();
						stack.push(""+cal(op, num1, num2));
					}
				}
			}
			System.out.println(stack.pop());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static int cal(String op, String num1, String num2)  {
		int a = Integer.parseInt(num1);
		int b = Integer.parseInt(num2);
		if("mul".equals(op)) {
			return mul(a, b);
		}
		if ("add".equals(op)) {
			return add(a, b);
		}
		if ("sub".equals(op)) {
			return sub(a, b);
		}
		if ("div".equals(op)) {
			return div(a, b);
		}
		throw new RuntimeException("Unknown operator: " + op);
	}

	// 输入:(mul 3-7)输出:-21
	public static int mul(int a, int b) {
		return a * b;
	}

	// 输入:(add 1 2)输出:3
	public static int add(int a, int b) {
		return a + b;
	}

	// 输入:(sub(mul 2 4)(div 9 3))输出:5
	public static int sub(int a, int b) {
		return a - b;
	}

	//输入:(div 1 0)输出:error
	public static int div(int a, int b) {
		if (b == 0) {
			throw new RuntimeException("error");
		}
		int result = a / b;
		if (result < 0) {
			if (a%b < 0) {
				return a/b - 1;
			}
		}
		return a / b;
	}
}
