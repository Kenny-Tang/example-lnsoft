package com.redjujubetree.huawei;

public class Input {
	public String[] lines;
	public int index = 0;
	public Input(String lines) {
		this.lines = lines.split("\n");
	}
	public String nextLine() {
		return lines[index++];
	}

	public boolean hasNextLine() {
		return index < lines.length;
	}
}
