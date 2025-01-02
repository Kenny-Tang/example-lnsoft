package com.redjujubetree.example.algorithm;

import java.util.*;

public class StudentScore {
	static Input input;
	static {
		input = new Input("01201022,75;01202033,95;01202018,80;01203006,90;01202066,100\n" +
				"01202008,70;01203102,85;01202111,80;01201021,75;01201100,88");
		input = new Input("01202021,75;01201033,95;01202008,80;01203006,90;01203088,100\n" +
				"01202008,70;01203088,85;01202111,80;01202021,75;01201100,88");
	}

	public static void main(String[] args) {
		Map<String, Score> scores = new HashMap<>();
		String[] course1 = input.nextLine().split(";");
		for (int i = 0; i < course1.length; i++) {
			String[] cs = course1[i].split(",");
			Score score = new Score(cs[0], 1, Integer.parseInt(cs[1]));
			scores.put(score.student_no, score);
		}

		String[] course2 = input.nextLine().split(";");
		TreeMap<String, TreeSet<Score>> treeMap = new TreeMap<>();
		for (int i = 0; i < course2.length; i++) {
			String[] cs = course2[i].split(",");
			Score score = new Score(cs[0], 1, Integer.parseInt(cs[1]));
			Score s1 = scores.get(score.student_no);
			if (s1 != null) {
				// 如果同时选修了两门课程，放到map等待输出
				score.course2_score = Integer.parseInt(cs[1]);
				TreeSet<Score> orDefault = treeMap.getOrDefault(s1.class_num, new TreeSet<>());
				orDefault.add(score);
				treeMap.put(s1.class_num, orDefault);
			}
		}
		if (treeMap.isEmpty()) {
			System.out.println("NULL");
			return;
		}

		for (Map.Entry<String, TreeSet<Score>> entry : treeMap.entrySet()) {
			System.out.println(entry.getKey());
			ArrayList<String> list = new ArrayList<>() ;
			for (Score score : entry.getValue()) {
				list.add(score.student_no);
			}
			System.out.println(String.join(",", list));
		}

	}

	static class Score implements Comparable<Score>{
		public int course1_score;
		public int course2_score;
		public String class_num;
		public String student_no;

		public Score(String student_no, int course_num, int score) {
			this.student_no = student_no;
			if (course_num == 1) {
				this.course1_score = score;
			} else {
				this.course2_score = score;
			}
			class_num = student_no.substring(0,5);
		}
		public int compareTo(Score other) {
			// 按照两门选修课成绩和的降序，成绩和相同时按照学号升序
			if (this.sum() == other.sum()) {
				return this.student_no.compareTo(other.student_no);
			}
			return other.sum() - this.sum();
		}
		public int sum() {
			return course1_score + course2_score;
		}
	}
}
