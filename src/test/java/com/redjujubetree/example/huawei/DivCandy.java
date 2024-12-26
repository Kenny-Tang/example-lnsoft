package com.redjujubetree.example.huawei;

import java.util.Scanner;

public class DivCandy {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int div = 0;
		while (n > 1) {
			// 如果末位不等于0， 说明为偶数，不需要拿或者放
			if ((n & 1) == 0) {
				n = n >> 1;
			} else if ((n & 2) == 0) {
				// 如果倒数第二位为 0  则减去一次可以消除至少两位
				n = n - 1;
			} else {
				// n 等于 3 的时候，减一位分一次可以为0
				if (n == 3) {
					n = n - 1;
				} else if ( n > 3 ) {
					// 其他情况说明有多个1 ， 增加一个可以消除多个1 ，使拿去糖果的次数降为最小
					n += 1;
				}
			}
			div++;
		}
		System.out.println(div);
	}
}
