package com.redjujubetree.example.algorithm;

public class MemmoryMgr {

	static Input input;
	static {
		input = new Input("5\n" +
				"REQUEST=10\n" +
				"REQUEST=20\n" +
				"RELEASE=0\n" +
				"REQUEST=20\n" +
				"REQUEST=10");
	}

	static int[] memmory = new int[100];

	public static void main(String[] args) {
		Integer count = Integer.parseInt(input.nextLine());

		for (int i = 0; i < count; i++) {
			String[] split = input.nextLine().split("=");
			if ("RELEASE".equals(split[0])) {
				RELEASE(Integer.parseInt(split[1]));
			} else if ("REQUEST".equals(split[0])) {
				System.out.println(REQUEST(Integer.parseInt(split[1])));
			}
		}
	}

	public static String REQUEST(int size) {
		if (size == 0) {
			return  "error";
		}
		int addr = 0;
		int count = 0;
		for (int i = 0; i < memmory.length;) {
			if (memmory[i] == 0) {
				count++;
				if (count == size) {
					memmory[addr] = size;
					return addr + "";
				}
				i++;
			} else {
				addr = i+ memmory[i];
				i = addr;
				count = 0;
			}
		}
		return "error";
	}

	public static void RELEASE(int addr) {
		if (memmory[addr] == 0) {
			System.out.println("error");
		} else{
			memmory[addr] = 0;
		}
	}
}
