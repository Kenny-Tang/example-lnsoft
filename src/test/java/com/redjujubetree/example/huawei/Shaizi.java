package com.redjujubetree.example.huawei;

public class Shaizi {
	// 0    1   2               3  4   5
	// 左1，右2，前3(观察者方向)，后4，上5，下6
	static int[] shaizi = {1,2,3,4,5,6};

	static Input input;
	static {
		input = new Input("RA");
	}
	public static void main(String[] args) {
		String s = input.nextLine();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == 'R') {
				R();
			}
			if (s.charAt(i) == 'A') {
				A();
			}
			if (s.charAt(i) == 'B') {
				B();
			}
			if (s.charAt(i) == 'C') {
				C();
			}
			if (s.charAt(i) == 'F') {
				F();
			}
			if (s.charAt(i) == 'L') {
				L();
			}
		}
		for (int i = 0; i < shaizi.length; i++) {
			System.out.print(shaizi[i]);
		}
		System.out.println();
	}
//	可以向左翻转(用L表示向左翻转1次)，
	public static void L() {
		int temp = shaizi[0];
		shaizi[0] = shaizi[4];
		shaizi[4] = shaizi[1];
		shaizi[1] = shaizi[5];
		shaizi[5] = temp;
	}
//
//	可以向右翻转(用R表示向右翻转1次)，
	public static void R() {
		int temp = shaizi[1];
		shaizi[1] = shaizi[4];
		shaizi[4] = shaizi[0];
		shaizi[0] = shaizi[5];
		shaizi[5] = temp;
	}
//
//	可以向前翻转(用F表示向前翻转1次)，
	public static void F() {
		int temp = shaizi[2];
		shaizi[2] = shaizi[4];
		shaizi[4] = shaizi[3];
		shaizi[3] = shaizi[5];
		shaizi[5] = temp;
	}
//
//	可以向后翻转(用B表示向右翻转1次)，
	public static void B() {
		int temp = shaizi[2];
		shaizi[2] = shaizi[5];
		shaizi[5] = shaizi[3];
		shaizi[3] = shaizi[4];
		shaizi[4] = temp;
	}
//
//	可以逆时针旋转(用A表示逆时针旋转90度)，
	public static void A() {
		int temp = shaizi[2];
		shaizi[2] = shaizi[0];
		shaizi[0] = shaizi[3];
		shaizi[3] = shaizi[1];
		shaizi[1] = temp;
	}
//
//	可以顺时针旋转(用C表示顺时针旋转90度)，
	public static void C() {
		int temp = shaizi[2];
		shaizi[2] = shaizi[1];
		shaizi[1] = shaizi[3];
		shaizi[3] = shaizi[0];
		shaizi[0] = temp;
	}
}
