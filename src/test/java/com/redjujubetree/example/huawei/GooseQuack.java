package com.redjujubetree.example.huawei;

import java.util.ArrayList;

public class GooseQuack {
	static  Input input;
	static {
		input = new Input("quackquack");
		input = new Input("qaauucqcaa");
		input = new Input("quacqkuackquack");
		input = new Input("qququaauqccauqkkcauqqkcauuqkcaaukccakkck");
		input = new Input("quacqkuquacqkacuqkackuack");
	}


	public static void main(String[] args) {
		String string = input.nextLine();

		String quack = "quack";
		int quackLength = quack.length();

		ArrayList<Integer> gooseQuack = new ArrayList<>();
		for (int i = 0; i < string.length(); i++) {
			int waitIndex = quack.indexOf(string.charAt(i));
			if (waitIndex == -1) {
				continue;
			}
			boolean addGoose = true;
			for (int j = 0; j < gooseQuack.size(); j++) {
				if (gooseQuack.get(j) % quackLength == waitIndex) {
					gooseQuack.set(j, gooseQuack.get(j) + 1);
					addGoose = false;
					break;
				}
			}
			if (addGoose && waitIndex == 0) {
				gooseQuack.add(1);
			}
		}
		int count = 0;
		for (int i = 0; i < gooseQuack.size(); i++) {
			if (gooseQuack.get(i) >= quackLength) {
				count++;
			}
		}
		if (count > 0) {
			System.out.println(count);
		} else{
			System.out.println(-1);
		}
	}

}
