package com.redjujubetree.huawei;

public class Sushi {

	static Input input ;
	static  {
		input = new Input("3 14 15 6 5");
	}

	public static void main(String[] args) {
		String[] ss = input.nextLine().split(" ");
		int[] table = new int[ss.length];
		for (int i = 0; i < ss.length; i++) {
			table[i] = Integer.parseInt(ss[i]);
		}

		int[] price = new int[ss.length];
		for (int i = 0; i < table.length; i++) {
			int p = table[i];
			for (int j = 0; j < table.length; j++) {
				int ind = (j+i) % table.length;
				if (table[ind] < p) {
					p += table[ind];
					break;
				}
			}
			price[i] = p;
		}
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < price.length; i++) {
			sb.append(price[i]).append(" ");
		}
		System.out.println(sb.toString().trim());
	}
}
