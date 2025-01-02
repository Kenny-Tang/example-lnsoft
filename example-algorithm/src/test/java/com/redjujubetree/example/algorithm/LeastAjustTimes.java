package com.redjujubetree.example.algorithm;

public class LeastAjustTimes {

	static Input input ;
	static  {
		input = new Input("3\n" +
				"head add 1\n" +
				"head add 2\n" +
				"head add 3\n" +
				"remove\n" +
				"remove\n" +
				"remove");
		input = new Input("3\n" +
				"head add 1\n" +
				"remove\n" +
				"tail add 2\n" +
				"head add 3\n" +
				"remove\n" +
				"remove");
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(input.nextLine());
		int size = 0;
		int ajust = 0;
		for (int i = 0; i < n*2; i++) {
			String command = input.nextLine();
			if (command.contains("remove")) {
				size--;
			}
			if (command.contains("tail")) {
				size++;
			}
			if (command.contains("head")) {
				if (size != 0) {
					ajust++;
				}
				size++;
			}
		}
		System.out.println(ajust);
	}
}
