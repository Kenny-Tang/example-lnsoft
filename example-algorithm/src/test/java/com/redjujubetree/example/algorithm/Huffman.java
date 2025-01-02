package com.redjujubetree.example.algorithm;

import java.util.PriorityQueue;

public class Huffman {
	static Input input;
	static {
		input = new Input("5\n" +
				"5 15 40 30 10");
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		String[] ss = input.nextLine().split(" ");
		PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>();

		for (int i = 0; i < n; i++) {
			priorityQueue.add(new ListNode(Integer.parseInt(ss[i]), 1));
		}
		while ((priorityQueue.size() > 1) ) {
			ListNode left = priorityQueue.poll();
			ListNode right = priorityQueue.poll();
			ListNode root = new ListNode(left.val + right.val, Math.max(left.height, right.height));
			root.left = left;
			root.right = right;
			priorityQueue.add(root);
		}
		dfs(priorityQueue.poll());
	}

	private static void dfs(ListNode root) {
		if (root == null) return;
		dfs(root.left);
		System.out.print(root.val + " ");
		dfs(root.right);
	}

	static class ListNode implements Comparable<ListNode> {
		public int val;
		public int height;
		public ListNode left, right;
		public ListNode(int val, int height) {
			this.val = val;
			this.height = height;
		}

		public int compareTo(ListNode o) {
			if (this.val == o.val) {
				return this.height - o.height;
			}
			return this.val - o.val;
		}
	}
}
