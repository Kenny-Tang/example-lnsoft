package com.redjujubetree.example.algorithm;

public class ThreeTree {
	static  Input input ;
	static {
		input = new Input("5\n" +
				"5000 2000 5000 8000 1800");
		input = new Input("3\n" +
				"5000 4000 3000");
	}
	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		if (n == 1) {
			System.out.println(1);
		} else {

			String[] nodes = input.nextLine().split(" ");
			TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
			int hight = 1;
			for (int i = 1; i < n; i++) {
				hight = Math.max(insertNode(root, Integer.parseInt(nodes[i])), hight);
			}
			System.out.println(hight + 1);
		}
	}

	public static int insertNode(TreeNode root, int val) {
		if (root.val - 500 > val) {
			if (root.left != null) {
				return insertNode(root.left, val) + 1;
			} else {
				root.left = new TreeNode(val);
				return 1;
			}
		} else if (root.val + 500 > val) {
			if (root.right != null) {
				return insertNode(root.right, val) + 1;
			} else {
				root.right = new TreeNode(val);
				return 1;
			}
		} else {
			if (root.center != null) {
				return insertNode(root.center, val) + 1;
			} else{
				root.left = new TreeNode(val);
				return 1;
			}
		}
	}
}
