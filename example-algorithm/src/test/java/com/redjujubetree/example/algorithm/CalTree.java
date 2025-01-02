package com.redjujubetree.example.algorithm;

public class CalTree {

	static Input input;
	static {
		input = new Input(
				"-3 12 6 8 9 -10 -7\n" +
				"8 12 -3 6 -10 9 -7");
		input = new Input(
				"7 -2 6 6 9\n" +
						"6 7 -2 9 6");
	}

	static  StringBuffer result = new StringBuffer();
	public static void main(String[] args) {
		String[] s1 = input.nextLine().split(" ");
		String[] s2 = input.nextLine().split(" ");
		int[] mid = new int[s1.length];
		for (int i = 0; i < s1.length; i++) {
			mid[i] = Integer.parseInt(s1[i]);
		}
		int[] pre = new int[s2.length];
		for (int i = 0; i < s2.length; i++) {
			pre[i] = Integer.parseInt(s2[i]);
		}
		TreeNode root = rebuild(mid, pre);
		merge(root);
		dfs(root);
		System.out.println(result.toString().trim());
	}

	public static void dfs(TreeNode root) {
		if (root == null) {
			return;
		}
		dfs(root.left);
		result.append(root.val + " ");
		dfs(root.right);
	}

	public static int merge(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int left = merge(root.left);
		int right = merge(root.right);
		int ret = root.val + left + right;
		root.val =  left + right;
		return ret;
	}

	public static TreeNode rebuild(int[] mid, int[] pre) {
		if (mid.length == 1){
			return new TreeNode(mid[0]);
		}
		if (mid.length == 0){
			return null;
		}
		TreeNode root = new TreeNode(pre[0]);

		int index = -1;
		for (int i = 0; i < mid.length; i++) {
			if (root.val == mid[i]){
				index =	i;
				break;
			}
		}

		int[] leftMid = new int[index];
		int[] leftPre = new int[index];
		for (int i = 0; i < index; i++) {
			leftMid[i] = mid[i];
			leftPre[i] = pre[i+1];
		}
		root.left = rebuild(leftMid, leftPre);
		int len = mid.length - leftMid.length - 1;
		int[] rightMid = new int[len];
		int[] rightPre = new int[len];

		for (int i = index + 1, j = 0; i < mid.length; i++, j++) {
			rightMid[j] = mid[i];
			rightPre[j] = pre[i];
		}
		root.right = rebuild(rightMid, rightPre);
		return root;
	}

	static class TreeNode{
		public int val;
		public TreeNode left;
		public TreeNode right;
		public TreeNode(int val) {
			this.val = val;
		}
	}
}
