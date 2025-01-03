package com.redjujubetree.huawei;

import java.util.*;

public class Books {
	static Input input;
	static {
		input = new Input("[[20,16],[15,11],[10,10],[9,10]]");
	}

	public static void main(String[] args) {
		String s = input.nextLine();
		for (String string : Arrays.asList("\\[", "]")) {
			s = s.replaceAll(string, "");
		}
		String[] split = s.split(",");
		List<Book> books = new ArrayList<>();
		for (int i = 0; i < split.length; i += 2) {
			books.add(new Book(Integer.parseInt(split[i]), Integer.parseInt(split[i + 1])));
		}
		Collections.sort(books);
		int[] dp = new int[books.size()];
		Arrays.fill(dp, 1);
		for (int i = 1; i < books.size(); i++) {
			int max = 1;
			for (int j = 0; j < i; j++) {
				if (books.get(j).height > books.get(i).height && books.get(j).width > books.get(i).width) {
					max = Math.max(max, dp[j] + 1);
				}
			}
			dp[i] = max;
		}
		int max = 1;
		for (int i = 0; i < dp.length; i++) {
			max = Math.max(max, dp[i]);
		}
		System.out.println(max);
	}
	static class Book implements Comparable<Book>{
		public int height;
		public int width;
		public Book(int height, int width) {
			this.height = height;
			this.width = width;
		}

		public int compareTo(Book o) {
			if (this.height == o.height) {
				return o.width - this.width;
			}
			return o.height - this.height;
		}

		@Override
		public String toString() {
			return this.height + " x " + this.width;
		}
	}
}
