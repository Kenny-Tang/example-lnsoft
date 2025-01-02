package com.redjujubetree.example.algorithm;

public class TLV {
	static Input input;
	static {
		input = new Input("31\n" +
				"32 01 00 AE 90 02 00 01 02 30 03 00 AB 32 31 31 02 00 32 33 33 01 00 CC");
	}

	public static void main(String[] args) {
		String target = input.nextLine();
		String[] flow = input.nextLine().split(" ");

		for (int i = 0; i < flow.length; ) {
			String tag = flow[i];
			i++;
			Integer length = Integer.parseInt( flow[i + 1]+flow[i], 16);
			i += 2;
			if (tag.equals(target)) {
				for (int j = i; j < i+length; j++) {
					System.out.print(flow[j] + " ");
				}
				System.out.println();
				return;
			} else {
				i+=length;
			}
		}
	}
}
