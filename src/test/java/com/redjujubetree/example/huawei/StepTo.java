package com.redjujubetree.example.huawei;

public class StepTo {

	static Input input;
	static {
		input = new Input("7 5 9 4 2 6 8 3 5 4 3 9");
		input = new Input("1 2 3 7 1 5 9 3 2 1");
	}
	public static void main(String[] args) {
		int steps = 1000;
		String[] split = input.nextLine().split(" ");
		int[] array = new int[split.length];

		for (int i = 0; i < split.length; i++) {
			array[i] = Integer.parseInt(split[i]);
		}

		// 如果最后一脚 迈到终点 则说明该这一步的起脚点 下标 + 对应的之恰好是中调的坐标
		// 依次递归个逻辑即可找到路径 计算该路径的步数即可
		for (int i = array.length - 2; i > 0; i--) {
			if (array[i] + i == array.length - 1) {
				int steps1 = getSteps(array, i);
				if (steps1 > 0) {
					steps = Math.min(steps, i);
				}
			}
		}
		if (steps == 1000) {
			System.out.println(-1);
		} else {
			System.out.println(steps);
		}

	}

	public static int getSteps(int[] array, int step) {
		// 如果这一步落在数组的前半部分， 第一步迈到这里即可以到达终点
		if ( step >= 1 && step < (array.length - 1)/2) {
			return 2;
		}
		// 如果起脚点小于 1 说明没有合适的起脚点
		if (step < 1) {
			return -1000;
		}
		int nextStep = -1000;
		// 找到上一脚的起脚点
		for (int i = step - 1; i > 0; i--) {
			if (array[i] + i == step) {
				nextStep = i;
			}
		}
		return  getSteps(array, nextStep) + 1;
	}
}
