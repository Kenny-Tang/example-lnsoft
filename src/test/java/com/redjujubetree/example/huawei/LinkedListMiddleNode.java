package com.redjujubetree.example.huawei;

import java.util.HashMap;
import java.util.Map;

public class LinkedListMiddleNode {

	static Input input;
	static {
		input = new Input("00010 4\n" +
				"00000 3 -1\n" +
				"00010 5 12309\n" +
				"11451 6 00000\n" +
				"12309 7 11451");
	}

	public static void main(String[] args) {
		String[] split = input.nextLine().split(" ");
		int first = Integer.parseInt(split[0]);
		int count = Integer.parseInt(split[1]);

		Map<Integer, Node> map = new HashMap<>(count);
		for (int i = 1; i <= count; i++) {
			String[] ss = input.nextLine().split(" ");
			Node node = new Node(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]));
			map.put(node.id, node);
		}

		for (Map.Entry<Integer, Node> entry : map.entrySet()) {
			Node node = entry.getValue();
			if (node.nextIndex != -1) {
				node.next = map.get(node.nextIndex);
			}
		}
		Node firstNode = map.get(first);

		if (firstNode.next == null) {
			System.out.println(firstNode.value);
			return;
		}
		Node p1 = firstNode;
		Node p2 = firstNode;
		while (p2 != null) {
			if (p2.next == null) {
				System.out.println(p1.value);
				return;
			}
			if (p2.next.next == null) {
				System.out.println(p1.next.value);
			}
			p1 = p1.next;
			p2 = p2.next.next;
		}

	}

	static class Node {
		public int id;
		public int value;
		public Node next;
		public int nextIndex;
		public Node(int id ,int value, int nextIndex) {
			this.id = id;
			this.value = value;
			this.nextIndex = nextIndex;
		}
	}
}
