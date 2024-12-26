package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.List;

public class VLANQ {
	static Input input;
	static {
		input = new Input("20-21,15,18,30,5-10\n" +
				"15");
		input  = new Input("5,1-3\n" +
				"10");
		input = new Input("1-5\n2");
	}

	public static void main(String[] args) {
		boolean[] vlans = new boolean[4096];
		String[] ls = input.nextLine().split(",");

		for (int i = 0; i < ls.length; i++) {
			String ds[] = ls[i].split("-");
			int start = Integer.parseInt(ds[0]);
			if (ds.length == 1) {
				vlans[start] = true;
			} else {
				int end = Integer.parseInt(ds[1]);
				for (int j = start; j <= end; j++) {
					vlans[j] = true;
				}
			}
		}

		Integer apply = Integer.parseInt(input.nextLine());
		vlans[apply] = false;

		List<String> list = new ArrayList<>();
		List<Integer> builder = new ArrayList<>();
		for (int i = 1; i < vlans.length; i++) {
			if (vlans[i]) {
				builder.add(i);
			} else {
				if (builder.size() >= 2){
					list.add(builder.get(0) + "-" + builder.get(builder.size() - 1));
					builder.clear();
				}
				if (builder.size() == 1){
					list.add(builder.get(0)+"");
					builder.clear();
				}
			}
		}
		System.out.println(String.join(",", list));
	}
}
