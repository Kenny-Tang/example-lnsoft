package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.List;

public class Kaoqin {

	static Input input ;

	static  {
		input = new Input("2\n" +
				"present\n" +
				"present present");
		input = new Input("2\n" +
				"present\n" +
				"present absent present present leaveearly present absent");
	}

	public static void main(String[] args) {
		int count = Integer.parseInt(input.nextLine());
		List<String> result = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			String[] details = input.nextLine().split(" ");
			boolean need = needToReward(details);
			if (need) {
				result.add("true");
			} else {
				result.add("false");
			}
		}

		System.out.println(String.join(" ", result));
	}

	public static boolean needToReward(String[] details) {
		String absent = "absent" ;
		String late = "late" ;
		String leaveearly = "leaveearly";
		String present = "present" ;

		int absentCount = 0;
		for (int i = 0; i < details.length; i++) {
			if (details[i].equals(absent)) {
				absentCount++;
				if (absentCount > 1) {
					return false;
				}
			}
			if (i > 0){
				if (late.equals(details[i]) && late.equals(details[i -1 ])) {
					return false;
				}
				if (leaveearly.equals(details[i]) && leaveearly.equals(details[i - 1])){
					return false;
				}
			}
		}

		int left = 0;
		int right = 0;
		int unCount = 0;
		while (left <= right && right < details.length) {
			if (!present.equals(details[right])) {
				unCount++;
			}
			if (unCount >= 3) {
				return false;
			}
			if (right - left < 7) {
				right++;
			} else {
				left++;
				right++;
			}
		}
		if (unCount >= 3) {
			return false;
		}
		return true;

	}
}
