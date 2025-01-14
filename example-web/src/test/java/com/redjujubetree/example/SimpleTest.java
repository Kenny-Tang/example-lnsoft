package com.redjujubetree.example;

import org.junit.jupiter.api.Test;

import java.util.*;

public class SimpleTest {

	@Test
	public void testAdd() {
		System.out.println((15|0x10) << 10);
		System.out.println(7 << 4);
	}

	@Test
	public void testNull() {
		System.out.println( null == null) ;
	}

	@Test
	public void testReverse() {
		System.out.println(new StringBuffer("reverse").reverse().toString());
	}

	@Test
	public void test() {
		String[] split = "127..56.50".split("\\.");
		for (String s : split) {
			System.out.println(Integer.valueOf(s));
		}
	}

	public void testss() {
		Collections.sort(new ArrayList<Character>(), new Comparator<Character>(){
			@Override
			public int compare(Character o1, Character o2) {
				return 0;
			}

			public int compareTo(Character o1, Character o2) {
				return 1;
			}
		});
	}

	@Test
	public void testMatch() {
		System.out.println('A' - 'a');
		System.out.println("11111".matches("[1]{1,}"));
		System.out.println("00111".matches("[1]{1,}"));
		System.out.println("11100".matches("[1]{1,}[0]{1}"));
	}

	@Test
	public void testJoin() {
		ArrayList<String> list = new ArrayList<>();
		list.add("A");
		list.add("B");
		list.add("C");
		;
		System.out.println(String.join(",", list));

		char[] charArray = "sdfasdf".toCharArray();
		System.out.println(new String(charArray).toString());
	}

	@Test
	public void testMap() {
		PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
		priorityQueue.add(1);
		priorityQueue.add(2);
		priorityQueue.add(3);
		System.out.println(priorityQueue.peek());
		PriorityQueue<Integer> priorityQueue2 = new PriorityQueue<>((o1, o2) -> o2 - o1);
		priorityQueue2.add(1);
		priorityQueue2.add(2);
		priorityQueue2.add(3);
		System.out.println(priorityQueue2.peek());

		System.out.println(Integer.valueOf("1".charAt(0)+""));
		System.out.println(Integer.valueOf("1000000000"));
											// 1000000000
	}

	@Test
	public void testREg() {
		String[] split = "i*am3a&&&body".split("[^A-Za-z]");
		LinkedList<String> list = new LinkedList<>();
		for (String s : split) {
			if (s.length() > 0) {
				list.addFirst(s);
			}
		}
		System.out.println(String.join(" ", list));

		System.out.println("Aa1(".charAt(3) >= '0');
		String pass = "Aa1(";
		for (int i = 0; i < pass.length(); i++) {
			System.out.println(pass.charAt(i) >= '0' && pass.charAt(i) <= '9');
		}

	}

	@Test
	public void test_index() {
		System.out.println("1234567".substring(0,5));
	}

}
