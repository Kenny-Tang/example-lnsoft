package com.redjujubetree.example.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Peach {

	Input input;

	@BeforeEach
	public void init() {
		// 测试用例
		input = new Input("30 11 23 4 20\n" +
			"6");
		input = new Input("2 3 4 5\n" +
			"4");
	}
	@Test
	public void peach() {
		String[] split = input.nextLine().split(" ");
		// split.length 数组长度为桃树数量
		int[] Ns = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			// 转化为整型数据
			Ns[i] = Integer.parseInt(split[i]);
		}
		// 获取守卫离开时间
		int H = Integer.valueOf(input.nextLine());
		int speed = speed(Ns, H);
		System.out.println(speed);
	}

	/**
	 * 计算最小速度
	 * @param Ns  桃树信息
	 * @param H	守卫离开时间
	 * @return 孙悟空吃桃子的最小速度
	 */
	public int speed(int[] Ns, int H) {
		if (Ns.length > H) {
			return 0;
		}
		int right = 100000; // 设置右边界
		int left = 0;	// 设置左边界
		while (left < right) {// 退出条件
			int mid = (right + left) / 2;
			int time = 0; // 在速度 mid 下吃完所有桃子所需要的时间
			for (int i = 0; i < Ns.length; i++) {
				// 计算吃完一棵桃树所需要的时间
				time += (Ns[i] + mid - 1) / mid;
			}
			// 小于守卫离开的时间，说明在守卫回来前不能将桃子吃完，需要加快速度，缩小左边界
			if (time > H) {
				left = mid + 1;
			} else {
				// 否则说明在守卫回来前，可以将桃子吃完，尝试使用更小的速度
				right = mid ;
			}
		}
		// 退出时 左右相等 即我们需要的速度
		return  left;
	}
}
