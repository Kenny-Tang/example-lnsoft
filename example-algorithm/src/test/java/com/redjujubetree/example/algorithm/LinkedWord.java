package com.redjujubetree.example.algorithm;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LinkedWord {

	@Test
	public void test() {
		Input input = new Input("The furthest distance in the world, ls not between life and death, But when I stand in front of you, Yet you don't know that I love you.\n" +
				"f");
		input  = new Input("I love you\n" +
				"He");
		String sentence = input.nextLine();
		String pre = input.nextLine();
		String[] split = sentence.split("[^a-zA-Z]");
		Map<Character, TreeSet<String>> map = new HashMap<>();
		for (String word : split) {
			if (word.length() != 0) {
				Set<String> set = map.getOrDefault(word.charAt(0), new TreeSet<>());
				set.add(word);
				map.put(word.charAt(0), new TreeSet<>(set));
			}
		}
		Set<String> set = map.get(pre.charAt(0));
		if (set != null && set.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String word : set) {
				if (word.startsWith(pre)) {
					sb.append(word).append(" ");
				}
			}
			System.out.println(sb.toString().trim()) ;
		} else {
			System.out.println(pre);
		}
	}
 }
