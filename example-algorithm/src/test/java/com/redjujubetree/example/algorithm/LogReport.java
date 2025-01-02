package com.redjujubetree.example.algorithm;

import org.junit.jupiter.api.Test;

public class LogReport {

	@Test
	public void report() {
		String line = "3 7 40 10 60";
		line = "50 60 1";
		String[] split = line.split(" ");
		int[] logs = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			logs[i] = Integer.valueOf(split[i]) ;
		}
		int max = getMax(logs);
		System.out.println(max);
	}

	public int getMax(int[] logs) {
		int score = logs[0];
		if (score >= 100) {
			return score;
		}
		int unreported = logs[0];
		for (int i = 1; i < logs.length; i++) {
			if (unreported >= 100) {
				return score;
			}
			score = Math.max(score, (score - unreported + logs[i])/100*100);
			unreported += logs[i] ;
		}
		return score;
	}
}
