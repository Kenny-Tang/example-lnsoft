package com.redjujubetree.example.algorithm;

import java.util.ArrayList;
import java.util.List;

public class ReadCommand {

	static Input input ;
	static {
		input = new Input("123\";\"" +
				"COMMAND TABLE IF EXISTS \"UNITED STATE\";\n" +
				"       COMMAND A GREAT (\n" +
				"         ID ADSAB,\n" +
				"         download_length INTE-GER, -- test\n" +
				"         file_name TEXT,\n" +
				"         guid TEXT,\n" +
				"         mime_type TEXT\n" +
				"         notifica-tionid INTEGER,\n" +
				"        original_file_name TEXT,\n" +
				"        pause_reason_type INTEGER,\n" +
				"        resumable_flag INTEGER,\n" +
				"        start_time INTEGER,\n" +
				"        state INTEGER,\n" +
				"\n" +
				"        folder TEXT,\n" +
				"\n" +
				"        path TEXT,\n" +
				"\n" +
				"        total_length INTE-GER,\n" +
				"\n" +
				"        url TEXT\n" +
				"\n" +
				");" +
				"ssdfad s ---- sdfasdf");
	}

	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
		while (input.hasNextLine()) {
			String line = input.nextLine().trim();
			if (line.length() == 1 && line.charAt(0) == '\n') {
				continue;
			}
			builder.append(line).append("\n");
		}
		int len = builder.length();
		int[] tag = new int[len];

		// 引号的开始位置
		int start = -1;
		for (int i = 0; i < len; i++) {
			if ((builder.charAt(i) == '\'' || builder.charAt(i) == '"') && !plainChar(i, builder)) {
				if (start == -1) {
					start = i;
				} else {
					while (start <= i) {
						tag[start++] = 1;
					}
					start = -1;
				}
			}
		}

		for (int i = 1; i < len; i++) {
			if (builder.charAt(i) == '-' && builder.charAt(i-1) == '-' && !plainChar(i, builder)) {
				int p = i - 1;
				while (builder.charAt(p) != '\n') {
					tag[p++] = 2;
				}
				i = p;
			}
		}
		List<Integer> semicolon  = new ArrayList<>();
		semicolon.add(-1);
		for (int i = 0; i < len; i++) {
			if (builder.charAt(i) == ';' && tag[i] < 1 && !plainChar(i, builder)) {
				semicolon.add(i);
			}
		}
		semicolon.add(len);

		int result= 0;
		for (int i = 1; i < semicolon.size(); i++) {
			int l = semicolon.get(i - 1) + 1;
			int r = semicolon.get(i) - 1;
			boolean flag = false;
			while (l <= r) {
				if (builder.charAt(l) != ' ' && builder.charAt(l) != '\n' && tag[l] < 1 && builder.charAt(l) != '\t') {
					flag = true;
					break;
				}
				l++;
			}
			if (flag) {
				result++;
			}
		}
		System.out.println(result);
		// -- System.out.println(builder.toString());
	}

	public static boolean plainChar(int ch, StringBuilder builder) {
		int count = 0;
		while (ch - 1 >= 0) {
			if (builder.charAt(ch - 1) == '\\') {
				count++;
				ch--;
			} else {
				break;
			}
		}
		return count % 2 == 1;
	}

}
