package com.redjujubetree.huawei;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Taxi {

	Input input;
	@BeforeEach
	void setUp() {
		input = new Input("888888888");
	}

	@Test
	public  void real() {
		Map<Character, Character> map = new HashMap<>();
		map.put('0', '0');
		map.put('1', '1');
		map.put('2', '2');
		map.put('3', '3');
		map.put('5', '4');
		map.put('6', '5');
		map.put('7', '6');
		map.put('8', '7');
		map.put('9', '8');

		String s = input.nextLine();
		StringBuilder sb= new StringBuilder();
		// 将输入数据转换为我们通常习惯表示的 9 进制的数
		for (int i = 0; i < s.length(); i++) {
			sb.append(map.get(s.charAt(i)));
		}

		System.out.println(Integer.parseInt(sb.toString(), 9));
	}

}