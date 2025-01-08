package com.redjujubetree.huawei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class AMain {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		// 获取学生数量
		int students = scanner.nextInt();
		// 获取科目数量
		int subjects = scanner.nextInt();
		// 读取科目
		List<String> subjectNames = new ArrayList<>();
		for (int i = 0; i < subjects; i++) {
			subjectNames.add(scanner.next());
		}
		// 结束本行
		scanner.nextLine();
		List<StudentScore> studentScores = new ArrayList<>();
		// 逐行读取学生的成绩
		for (int i = 0; i < students; i++) {
			studentScores.add(new StudentScore(scanner.nextLine()));
		}
		// 获取排序科目
		String sortSubject = scanner.next();
		for (int i = 0; i < subjectNames.size(); i++) {
			if (subjectNames.get(i).equals(sortSubject)) {
				StudentScore.sortSubject = i;
			}
		}
		// 对学生成绩排序
		Collections.sort(studentScores);
		String res = "";
		for (int i = 0; i < studentScores.size(); i++) {
			res += studentScores.get(i).toString() + " ";
		}
		// 输出排序结果
		System.out.println(res.trim());
	}

	private static class StudentScore implements Comparable<StudentScore> {
		// 排序字段， 默认安按照总分排序
		public static int sortSubject = -1;
		// 学生总分
		private int totalScore = 0;
		// 学生姓名
		private String name ;
		// 学生各科目的成绩
		private List<Integer> scores = new ArrayList<>();
		public StudentScore(String studentScore) {
			String[] split = studentScore.split(" ");
			name = split[0];
			for (int i = 1; i < split.length; i++) {
				int score = Integer.parseInt(split[i]);
				scores.add(score);
				totalScore += score;
			}
		}

		// 成绩又高到低排序
		public int compareTo(StudentScore studentScore) {
			if (sortSubject > -1) {
				return  studentScore.scores.get(sortSubject) - scores.get(sortSubject);
			}
			return  studentScore.totalScore - this.totalScore;
		}
		// 方便输出
		public String toString() {
			return name;
		}
	}
}
