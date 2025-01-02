package com.redjujubetree.huawei;

import org.junit.jupiter.api.Test;

import java.util.*;

public class OD100 {

	/**
	 *
	 */
	@Test
	public void testPuker() {
		String s = "2 9 J 10 3 4 K A 7 Q A 5 6";
		String[] cards = s.split(" ");

		int[] cardsInt = new int[cards.length];
		for (int i = 0; i < cards.length; i++) {
			cardsInt[i] = encode.get(cards[i]);
		}
		Arrays.sort(cardsInt);
		int left = 0;
		int right = 1;

		List<String> list = new ArrayList<>();
		while (right < cardsInt.length) {
			if (cardsInt[left] == 2) {
				left++;
				right++;
				continue;
			}
			if (cardsInt[right] != cardsInt[right - 1] + 1) {
				if (right - left >= 5) {
					list.add(getString(cardsInt, left, right));
				}
				left = right;
			}
			right++;
		}
		for (String card : list) {
			System.out.println(card);
		}
	}

	public String getString(int[] cards, int left, int right) {
		StringBuilder sb = new StringBuilder();
		for (int i = left; i < right; i++) {
			sb.append(decode.get(cards[i]) + " ");
		}
		return sb.toString().trim();
	}
	static Map<String, Integer> encode = new HashMap<>();
	static Map<Integer, String> decode = new HashMap<>();
	static {
		encode.put("2", 2);
		encode.put("3", 3);
		encode.put("4", 4);
		encode.put("5", 5);
		encode.put("6", 6);
		encode.put("7", 7);
		encode.put("8", 8);
		encode.put("9", 9);
		encode.put("10", 10);
		encode.put("J", 11);
		encode.put("Q", 12);
		encode.put("K", 13);
		encode.put("A", 14);

		for (Map.Entry<String, Integer> entry : encode.entrySet()) {
			decode.put(entry.getValue(), entry.getKey());
		}
	}
}
