package com.redjujubetree.huawei;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.*;

public class Equipment {

	public static void main(String[] args) {
		InputStream inputStream = new StringBufferInputStream("500 3\n" +
				"6\n" +
				"0 80 100\n" +
				"0 90 200\n" +
				"1 50 50\n" +
				"1 70 210\n" +
				"2 50 100\n" +
				"2 60 150");
		inputStream = new StringBufferInputStream("100 1\n" +
				"1\n" +
				"0 90 200");
		Scanner scanner = new Scanner(inputStream);
		String[] split = scanner.nextLine().split(" ");
		int total = Integer.parseInt(split[0]);
		int buy = Integer.parseInt(split[1]);
		int sell = Integer.parseInt(scanner.nextLine());

		List<Equip> list = new ArrayList<>();
		for (int i = 0; i < sell; i++) {
			String[] ss = scanner.nextLine().split(" ");
			list.add(new Equip(Integer.parseInt(ss[0]), Integer.parseInt(ss[ 1]), Integer.parseInt(ss[2])));
		}
		Collections.sort(list);

		int max = -1;
		for (int i = 0; i < list.size(); i++) {
			int buy1 = buy(list, buy, total, i);
			if (buy1 < 0) {
				break;
			}
			max = Math.max(max, buy1);
		}
		System.out.println(max);

	}

	public static int buy(List<Equip> list, int buy, int total, int start) {
		Map<Integer, Equip> map = new HashMap<>();
		int result = list.get(start).reliability;
		for (int i = start; i < list.size(); i++) {
			Equip equip = list.get(i);
			Equip equip1 = map.get(equip.type);
			if (equip1 == null) {
				map.put(equip.type, equip);
				if (map.size() == buy) {
					break;
				}
			}
		}
		if (map.size() == buy) {
			int spent = 0;
			for (Map.Entry<Integer, Equip> entry : map.entrySet()) {
				spent += entry.getValue().price;
			}
			if (spent > total) {
				return -1;
			}
		} else {
			if (map.size() != buy) {
				return -1;
			}
		}
		return result;
	}



	static class Equip implements Comparable<Equip>{
		public Integer type;
		public Integer reliability;
		public Integer price;

		public Equip(int type, int reliability, int price) {
			this.type = (Integer) type;
			this.reliability = reliability;
			this.price = price;
		}

		public int compareTo(Equip o) {
			if (this.reliability == o.reliability) {
				return this.price - o.price;
			}
			return this.reliability - o.reliability;
		}
	}


}
