package com.redjujubetree.huawei;

public class KeepDistance {

	static Input input;
	static {
		input = new Input("10\n" +
				"[1,1,1,1,-4,1]");
	}
	static int[] seats;
	public static void main(String[] args) {
		int count = Integer.parseInt(input.nextLine());
		String[] ss = input.nextLine().replaceAll("[\\]\\[]", "").split(",");

		seats = new int[count];
		int last = -1;
		for (int i = 0; i < ss.length; i++) {
			int s = Integer.parseInt(ss[i]);
			if ( s < 0){
				seats[-s] = 0;
			} else {
				last = getNextSeat(seats);
				seats[last] = 1;
			}
		}
		System.out.println(last);
	}

	public static int getNextSeat(int[] arr) {
		int result = -1;
		int left = 0;
		int right = 0;
		int len = 0;
		int count = 0;
		while (right < arr.length) {
			if (arr[right] == 1) {
				count++;
				if (left < right) {
					// 新社交距离
					int location = (right - left) / 2 - 1;
					if (location > len) {
						// 新社交距离的下一个为要坐下的位置
						result = location + left + 1;
						len = location;
					}
					left = right;
				}
			} else if (right == arr.length - 1) {
				if (right - left - 1 > len) {
					result = arr.length - 1;
				}
			}
			right++;
		}
		if (count == 0) {
			result = 0;
		}
		if (count == 10) {
			return -1;
		}
		return result;
	}


}
