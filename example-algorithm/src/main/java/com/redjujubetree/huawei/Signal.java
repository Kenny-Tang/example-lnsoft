package com.redjujubetree.huawei;

public class Signal {
	static Input input ;
	static {
		input = new Input("0010101010110000101000010");
	}

	public static void main(String[] args) {
		String[] signals = input.nextLine().split("00");
		int max = Integer.MIN_VALUE;
		String result = "";
		for (int i = 0; i < signals.length; i++) {
			String signal = signals[i];
			if (signal.length() > 0 && signal.equals(signal.replace("11", ""))) {
				int length = signal.lastIndexOf('1') - signal.indexOf('1');
				if (length > max) {
					result = signal;
					max = length;
				}
			}
		}
		String substring = result.substring(result.indexOf('1'), result.lastIndexOf('1') + 1);
		System.out.println("0" + result + "0");
	}
}
