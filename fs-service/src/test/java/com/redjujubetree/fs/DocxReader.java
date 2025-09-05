package com.redjujubetree.fs;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class DocxReader {

	@Test
	public void test1() {
		String str = "jdbc:sqlite:/Users/kenny/IdeaProjects/database.db";
		String replace = str.replace("jdbc:sqlite:", "");
		System.out.println(replace.substring(0, replace.lastIndexOf(File.separator)));
		System.out.println(containsChineseByRegex(str)); // 输出: true
	}


	@Test
	public void test() throws TikaException, IOException {
		Tika tika = new Tika();
		String content = tika.parseToString(new File("/Users/kenny/IdeaProjects/example/fs-service/src/main/resources/frozen.docx"));

		OutputStream os = java.nio.file.Files.newOutputStream(new File("/Users/kenny/IdeaProjects/example/fs-service/src/main/resources/frozen.txt").toPath());
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))));
		String line;
		while ((line = reader.readLine()) != null) {
			if (containsChineseByRegex(line)) {
				continue;
			}
			if (line.trim().isEmpty()) {
				continue;
			}
			System.out.println(line+"\n");
			os.write(("    "+line+"\n\n").getBytes());
		}


	}


	// 预编译正则表达式，提高性能
	private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fff]");

	/**
	 * 方法1：使用正则表达式（推荐）
	 * Unicode范围：\u4e00-\u9fff 基本涵盖常用中文字符
	 */
	public static boolean containsChineseByRegex(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		return CHINESE_PATTERN.matcher(str).find();
	}

}
