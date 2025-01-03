package com.redjujubetree.huawei;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ScoreQueue {

	Input input;

	@BeforeEach
	void setUp() {
		input = new Input(
				"3 2\n" +
				"yuwen shuxue\n" +
				"fangfang 95 90\n" +
				"xiaohua 88 95\n" +
				"minmin 90 95\n" +
				"zongfen");
		input = new Input(
				"3 2\n" +
						"yuwen shuxue\n" +
						"fangfang 95 90\n" +
						"xiaohua 88 95\n" +
						"minmin 90 95\n" +
						"shuxue");
	}

	@Test
	public void sort() {

		String[] s = input.nextLine().split(" ");

		int studentNum = Integer.parseInt(s[0]);
		int subjectNum = Integer.parseInt(s[1]);

		String[] subjects = input.nextLine().split(" ");

		Map<String, Integer> map = new HashMap<>();
		for (int i = 0; i < subjectNum; i++) {
			map.put(subjects[i], i);
		}

		Score[] studentScores = new Score[studentNum];
		for (int i = 0; i < studentNum; i++) {
			studentScores[i] = new Score(input.nextLine());
		}
		String sortSubject = input.nextLine();
		Score.sortSubject = map.getOrDefault(sortSubject, subjectNum);
		Arrays.sort(studentScores);

		for (int i = 0; i < studentNum; i++) {
			System.out.print(studentScores[i].name + " " );
		}
		System.out.println();

	}

	static class Score implements Comparable<Score> {
		static volatile int sortSubject = -1;
		int[] scores;
		String name;
		public Score(String string) {
			String[] split = string.split(" ");
			scores = new int[split.length];
			name = split[0];
			int songfen = 0;
			for (int i = 1; i < split.length; i++) {
				scores[i-1] = Integer.parseInt(split[i]);
				songfen += scores[i-1];
			}
			scores[split.length - 1] = songfen;
		}

		@Override
		public int compareTo(Score o) {
			int x =  this.scores[sortSubject] - o.scores[sortSubject];
			if (x == 0) {
				return this.name.compareTo(o.name);
			}
			return x;
		}
	}
}
