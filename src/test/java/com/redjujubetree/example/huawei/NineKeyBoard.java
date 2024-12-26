package com.redjujubetree.example.huawei;

import java.util.HashMap;
import java.util.Map;

public class NineKeyBoard {
	static Input input;
	static Map<String, String[]> map = new HashMap<>();
	static {
		input = new Input("7 8\n" +
				"u x");
		input = new Input("7 8\nx");
		map.put("0", new String[]{"a","b","c"});
//		0 关联“a”,”b”,”c”
		map.put("1", new String[]{"d","e","f"});
//		1 关联“d”,”e”,”f”
		map.put("2", new String[]{"g","h","i"});
//		2 关联“g”,”h”,”i”
		map.put("3", new String[]{"j","k","l"});
//		3 关联“j”,”k”，”l”
		map.put("4", new String[]{"m","n","o"});
//		4 关联“m”,”n”,”o”
		map.put("5", new String[]{"p","q","r"});
//		5 关联“p”,”q”,”r”
		map.put("6", new String[]{"s","t"});
//		6 关联“s”，”t”
		map.put("7", new String[]{"u","v"});
//		7 关联“u”,”v”
		map.put("8", new String[]{"w","x"});
//		8 关联“w”,”x”
		map.put("9", new String[]{"y","z"});
//		9 关联“y”,”z”
	}

	public static void main(String[] args){
		String[] ss = input.nextLine().split(" ");
		String[] ex = input.nextLine().split(" ");
		dfs(ss, "", 0, ex);

	}
	public static void dfs(String[] ss, String tar, int i, String[] ex) {
		if (i == ss.length) {
			for (int x = 0; x < ex.length; x++) {
				if (!tar.contains(ex[x])) {
					System.out.print(tar+",");
					return;
				}
			}
			return;
		}
		String[] strings = map.get(ss[i]);
		for (int j = 0; j < strings.length; j++) {
			dfs(ss, tar + strings[j], i + 1, ex);
		}
	}
}
