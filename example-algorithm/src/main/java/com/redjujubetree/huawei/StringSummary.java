package com.redjujubetree.huawei;

import java.util.ArrayList;
import java.util.Collections;

public class StringSummary {
	static Input input;
	static {
		input = new Input("bAaAcBb#$%^&");
		input = new Input("aabbcc");
	}

	public static void main(String[] args) {
		String string = input.nextLine();
		string = string.toLowerCase().replaceAll("[^a-zA-Z]", "");
		int count = 1;
		ArrayList<Summary> list = new ArrayList<>();
		for (int i = 0; i < string.length();) {
			if (i == string.length() - 1) {
				list.add(new Summary(string.charAt(i),0));
				i++;
			}
			if (string.charAt(i) != string.charAt(i+1)) {
				int diff = getDiff(string.substring(i+1), string.charAt(i));
				list.add(new Summary(string.charAt(i), diff));
				i++;
			} else {
				int same = getSame(string, i);
				Summary summary = new Summary(string.charAt(i), same - i);
				list.add(summary);
				i= same;
			}
		}
		Collections.sort(list);
		for (Summary summary : list) {
			System.out.print(summary);
		}
		System.out.println();
	}

	public static int getSame(String a, int index) {
		char ch = a.charAt(index);

		for (int i = index; i< a.length(); i++) {
			if (ch != a.charAt(i)) {
				return i;
			}
		}
		return a.length() ;
	}

	public static int getDiff(String string, char ch) {
		int sum = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == ch) {
				sum++;
			}
		}
		return sum;
	}

	static class Summary implements Comparable<Summary>{
		public char ch;
		public int count;
		public Summary(char ch, int count){
			this.ch = ch;
			this.count = count;
		}

		public int compareTo(Summary o) {
			if (this.count == o.count) {
				return this.ch - o.ch;
			}
			return o.count - this.count;
		}

		public String toString(){
			return ch + "" + count;
		}

	}
}
