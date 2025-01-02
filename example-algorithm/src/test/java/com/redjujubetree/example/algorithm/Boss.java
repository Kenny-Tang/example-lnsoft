package com.redjujubetree.example.algorithm;

import java.util.*;

public class Boss {
	/*
5
1 0 100
2 0 199
3 0 200
4 2 200
5 0 200
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		Map<Integer, Node> map = new HashMap<>();

		int nodeId = -1;
		for (int i = 0; i < n; i++) {
			int id = scanner.nextInt();
			int parentId = scanner.nextInt();
			int income = scanner.nextInt();
			Node node = new Node(id, income);
			// 如果已经存在，则更新income
			if (map.containsKey(id)) {
				node = map.get(id);
				node.income = income;
			} else {
				// 如果不存在则直接放入map
				map.put(id, node);
			}
			if (!map.containsKey(parentId)) {
				// 上级节点不存在，则创建上级节点
				Node parentNode = new Node(parentId, 0);
				node.parent = parentNode;
				map.put(parentId, parentNode);
			}
			// 增加上级所管理的员工
			map.get(parentId).emps.add(node);
			nodeId = parentId;
		}
		map.get(nodeId);
		Node boss = map.get(nodeId);
		// 搜索Boss节点
		while (boss.parent != null) {
			boss = map.get(boss.parent);
		}
		// 计算总收入
		int total = dfs(boss);
		System.out.println(boss.id + " " + total);
	}

	public static int dfs(Node node) {
		if (node == null) {
			return 0;
		}

		int result = node.income;
		for (int i = 0; i < node.emps.size(); i++) {
			result += dfs(node.emps.get(i)) / 100 * 15;
		}
		return result;
	}

	static class Node{
		public int id ;
		public int income;
		public List<Node> emps = new ArrayList<>();
		public Node parent;
		public Node(int id, int income) {
			this.id = id;
			this.income = income;
		}
	}
}

