package com.redjujubetree.huawei;

import java.util.HashMap;
import java.util.Map;

public class DirManager {
	static Input input;
	static {
		input = new Input("mkdir abc/efg\n" +
				"mkdir abc\n" +
				"cd abc\n" +
				"pwd");
	}

	public static void main(String[] args) {
		Node current = new Node("/");
		while (input.hasNextLine()) {
			String line = input.nextLine();
			if (isValid(line)) {
				String[] ss = line.split(" ");
				if (line.startsWith("mkdir")) {
					current.mkdir(ss[1]);
				}
				if (line.startsWith("cd")) {
					Node cd = current.cd(ss[1]);
					if (cd != null) {
						current = cd;
					}
				}
				if (line.startsWith("pwd")) {
					System.out.println(current.pwd());
				}
			}
		}
	}
	static boolean isValid(String line) {
		if ("pwd".equals(line)) {
			return true;
		}
		String[] split = line.split("[^a-z\\.]");
		return split.length == 2;
	}

	static class Node{
		public String name;
		public Node parent;
		public Map<String, Node> children = new HashMap<>();
		public Node(String name) {
			this.name = name;
		}
		public boolean exists(String node) {
			return children.containsKey(node);
		}
		public boolean mkdir(String dir){
			if (!exists(dir)) {
				Node node = new Node(dir);
				node.parent = this;
				children.put(dir, node);
				return true;
			}
			return false;
		}

		public Node cd(String dir){
			if (exists(dir)) {
				return children.get(dir);
			}
			if ("..".equals(dir)) {
				if (this.parent != null) {
					return parent;
				}
				return this;
			}
			return null;
		}

		public String pwd(){
			Node node = this;
			String pwd = "";
			while (!node.name.equals("/")) {
				pwd = node.name + "/" + pwd;
				node = node.parent;
			}
			return "/" + pwd;
		}

	}
}
