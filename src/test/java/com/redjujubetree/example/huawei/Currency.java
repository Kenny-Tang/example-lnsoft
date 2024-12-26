package com.redjujubetree.example.huawei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Currency {

	static Input input;
	static Map<String, Double> map = new HashMap<>();
	static {
		input = new Input("2\n" +
				"20CNY53fen\n" +
				"53HKD87cents");
		map.put("CNY", 0.01);
		map.put("HKD",0.0123d);
		map.put("cents",1.23);
		map.put("JPY",0.1825d);
		map.put("sen",18.25d);
		map.put("EUR",0.0014d);
		map.put("eurocents",0.14);
		map.put("GBP",0.0012d);
		map.put("pence",0.12);
	}

	public static void main(String[] args) {
		Integer count = Integer.parseInt(input.nextLine());
		double sum = 0;
		for (int i = 0; i < count; i++) {
			String s = input.nextLine();
			sum += change(s);
		}
		long result = (long)sum;
		System.out.println(result);
	}

	public static double change(String s) {
		StringBuilder builder = new StringBuilder("");
		List<String> list = new ArrayList<>();
		s ="0"+s;
		for (int i = 1; i < s.length(); i++ ) {

			if (Character.isDigit(s.charAt(i))) {
				if (!Character.isDigit(s.charAt(i - 1))) {
					list.add(builder.toString());
					builder.setLength(0);
				}
			}

			if (!Character.isDigit(s.charAt(i))) {
				if (Character.isDigit(s.charAt(i - 1))) {
					list.add(builder.toString());
					builder.setLength(0);
				}
			}
			builder.append(s.charAt(i));
		}
		list.add(builder.toString());
		double sum = 0;
		for (int i = 1; i < list.size(); i+=2) {
			sum += Double.parseDouble(list.get(i-1)) / map.getOrDefault(list.get(i), 1d) ;
		}
		return sum;
	}

}

