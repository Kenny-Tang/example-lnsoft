package com.redjujubetree.huawei;

import java.util.ArrayList;
import java.util.List;

public class KNumber {

	static Input input ;
	static {
		input = new Input("3\n" +
				"3");
		input = new Input("5\n" +
				"96");
	}

	static  String[] numbers = new String[]{"1","2","3","4","5","6","7","8","9"};
	static  int n = 0;
	static  int k = 0;
	/*
	康托展开的逆运算
	既然康托展开是一个双射，那么一定可以通过康托展开值求出原排列，即可以求出n的全排列中第x大排列。

	如n=5,x=96时：

	首先用96-1得到95，说明x之前有95个排列.(将此数本身减去1)
	用95去除4! 得到3余23，说明有3个数比第1位小，所以第一位是4.
	用23去除3! 得到3余5，说明有3个数比第2位小，所以是4，但是4已出现过，因此是5.
	用5去除2!得到2余1，类似地，这一位是3.
	用1去除1!得到1余0，这一位是2.
	最后一位只能是1.
	所以这个数是45321.
	 */
	public static void main(String[] args) {
		n = Integer.parseInt(input.nextLine()) ;
		k = Integer.parseInt(input.nextLine());
		List<Integer> list = new ArrayList<>();


		int div = 1;
		for (int i = 1; i < n; i++) {
			div *= i;// 计算阶乘数
			list.add(i); // 填充使用到的数字
		}
		list.add(n);

		int x = k - 1;
		String result = "";
		for (int i = n-1; i > 0; i--) {
			// 计算阶乘数
			int num = x / div;
			x = x % div;
			div /= i;
			result += list.get(num); // 获取该位置上的数字
			list.remove(num); // 数字使用过后不允许再次使用
		}
		result += list.get(0);
		System.out.println(result);
	}

}