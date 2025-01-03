package com.redjujubetree.huawei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Expression {

	static Input input;
	static {
		input = new Input("2.3,3,5.6,7,6;11,3,8.6,25,1;0.3,9,5.3,66,7.8;1,3,2,7,5;340,670,80.6;<=,<=,<=");
		input = new Input("2.36,3,6,7.1,6;1,30,8.6,2.5,21;0.3,69,5.3,6.6,7.8;1,13,2,17,5;340,67,300.6;<=,>=,<=");
	}

	public static void main(String[] args) {
		String[] exps = input.nextLine().split(";");
		// 比较符号
		String[] symbols = exps[exps.length-1].split(",");
		// 第一个操作数
		ArrayList<ArrayList<Double>> opt_values = new ArrayList<>();
		for (int i = 0; i < symbols.length; i++) {
			String[] split = exps[i].split(",");
			ArrayList<Double> opt = new ArrayList<>();
			for (int j = 0; j < split.length; j++) {
				opt.add(Double.parseDouble(split[j]));
			}
			opt_values.add(opt);
		}

		// 第二个操作数
		ArrayList<Double> opt_values2 = new ArrayList<>();
		String[] vals = exps[symbols.length].split(",");
		for (int i = 0; i < vals.length; i++) {
			opt_values2.add(Double.parseDouble(vals[i]));
		}

		// 比较数
		ArrayList<Double> results = new ArrayList<>();
		String[] split = exps[exps.length - 2].split(",");
		for (int i = 0; i < split.length; i++) {
			results.add(Double.parseDouble(split[i]));
		}

		Map<String, String> map = new HashMap<>();
		map.put("eq", ">=,<=,=");
		map.put("gt", ">=,>");
		map.put("lt", "<=,<");

		// 计算表达式
		double max = Long.MIN_VALUE;
		boolean flag = true;
		for (int i = 0; i < symbols.length; i++) {
			ArrayList<Double> doubles = opt_values.get(i);
			double mul = 0;
			for (int j = 0; j < doubles.size(); j++) {
				double d = doubles.get(j) * opt_values2.get(j);
				mul += d;
			}
			Double sub = mul - results.get(i);
			boolean res;
			if ( sub == 0) {
				res = map.get("eq").contains(symbols[i]);
			} else if (sub > 0) {
				res = map.get("gt").contains(symbols[i]);
			} else {
				res = map.get("lt").contains(symbols[i]);
			}
			if (!res) {
				flag = false;
			}
			max = Math.max(max, sub);
		}
		System.out.println(flag + " " + ((long)max));
	}
}
