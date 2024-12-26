package com.redjujubetree.example.huawei;

public class KickSeven {

	static Input input;
	static  {
		input = new Input("0 0 0 2 1");
	}

	public static void main(String[] args) {
		String[] strings = input.nextLine().split(" ");
		int sevenCount = 0;
		for (String string : strings) {
			if (!"0".equals(string)) {
				sevenCount += Integer.parseInt(string);
			}
		}

		int n = strings.length;
		int[] counts = new int[n];
		int counter = 1;
		while (true) {
			for (int i = 0; i < n; i++) {
				if (counter % 7 == 0 || String.valueOf(counter).contains("7")) {
					counts[i]++;
					sevenCount--;
					if (sevenCount == 0) {
						StringBuilder builder = new StringBuilder();
						for (int j = 0; j < n; j++) {
							builder.append(counts[j] + " ");
						}

						System.out.println(builder.toString().trim());
						return;
					}
				}
				counter++;
			}
		}
	}
}
