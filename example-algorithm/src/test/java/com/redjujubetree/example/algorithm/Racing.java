package com.redjujubetree.example.algorithm;

import java.util.*;

public class Racing {

	static Input input;

	static {
		input = new Input("4,5\n" +
				"10,6,9,7,6\n" +
				"9,10,6,7,5\n" +
				"8,10,6,5,10\n" +
				"9,10,8,4,9");
		input = new Input("2,5\n" +
				"7,3, 5,4,2\n" +
				"8,5,4,4,3");
		input = new Input("4,2\n" +
				"8,5\n" +
				"5.6\n" +
				"10,4\n" +
				"8,9");
		input = new Input("4,5\n" +
				"11,6,9,7,8\n" +
				"9,10,6,7,8\n" +
				"8,10,6,9,7\n" +
				"9,10,8,6,7");
	}

	public static void main(String[] args) {
		try {
			String[] ss = input.nextLine().split(",");
			int referees = Integer.parseInt(ss[0]);
			int players = Integer.parseInt(ss[1]);
			if (players > 100 || players < 3 || referees > 10 || referees < 3) {
				System.out.println(-1);
				return;
			}
			// 解析打分输入
			int[][] socres = new int[referees][players];
			for (int i = 0; i < referees; i++) {
				if (!input.hasNextLine()) {
					System.out.println(-1);
					return;
				}
				String line = input.nextLine();
				String[] split = line.split(",");
				if (split.length != players) {
					System.out.println(-1);
					return;
				}
				for (int j = 0; j < players; j++) {
					int score = Integer.parseInt(split[j].trim());
					if (score < 1 || score > 10) {
						System.out.println(-1);
						return;
					}
					socres[i][j] = score;
				}
			}
			List<Player> playerList = new ArrayList<>();
			for (int i = 0; i < players; i++) {
				Integer[] scores = new Integer[referees];
				for (int j = 0; j < referees; j++) {
					scores[j] = socres[j][i];
				}
				Arrays.sort(scores, (o1, o2) -> o2 - o1);
				Player player = new Player(i+1, scores);
				playerList.add(player);
			}
			Collections.sort(playerList);
			StringBuilder append = new StringBuilder().append(playerList.get(0).no).append(",");
			append.append(playerList.get(1).no).append(",");
			append.append(playerList.get(2).no);
			System.out.println(append.toString());

		} catch (Exception e) {
			System.out.println(-1);
			e.printStackTrace();
		}
	}

	static class Player implements Comparable<Player>{
		public int no;
		public int score_total;
		Integer[] scores;
		public Player(int no, Integer[] scores) {
			this.no = no;
			this.scores = scores;
			for (int i = 0; i < scores.length; i++) {
				this.score_total += scores[i];
			}
		}

		public int compareTo(Player o) {
			if (this.score_total == o.score_total) {
				for (int i = 0; i < scores.length; i++) {
					if (this.scores[i] != o.scores[i]) {
						return o.scores[i] - this.scores[i];
					}
				}
			}
			return  o.score_total - this.score_total;
		}
	}

}
