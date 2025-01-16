package com.redjujubetree.huawei;

import java.util.Scanner;

public class AMain {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int count = scanner.nextInt();
		Node root = new Node(scanner.nextInt());
		int max = 1;
		for (int i = 1; i < count; i++) {
			max = Math.max(max, insert(root, new Node(scanner.nextInt()), 1));
		}
		System.out.println(max);
	}
	public static int insert(Node root, Node node, int hight) {
		// 插入节点后可得到的树的高度
		hight += 1 ;
		if (node.value < root.value - 500) {
			// - 如果数小于节点的数减去 500，则将数插入节点的左子树<br>
			if (root.left == null) {
				root.left = node;
				return hight;
			}
			return insert(root.left, node, hight);
		} else if (node.value > root.value + 500){
			// - 如果数大于节点的数加上 500，则将数插入节点的右子树
			if (root.right == null) {
				root.right = node;
				return hight;
			}
			return insert(root.right, node, hight);
		} else {
			// - 否则，将数插入节点的中子树
			if (root.middle == null) {
				root.middle = node;
				return hight;
			}
			return insert(root.middle, node, hight);
		}
	}

	static class Node{
		public Node left;
		public Node right;
		public Node middle;
		public Integer value;
		public Node(Integer value) {
			this.value = value;
		}
	}
}
