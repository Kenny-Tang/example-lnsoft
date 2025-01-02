package com.redjujubetree.huawei;

import java.util.*;

public class Tokenizer {

	static Map<Character, List<String>> map = new HashMap<>();
	static Input input;
	static {
		input = new Input("ilovechina\n" +
				"i,love,china,ch,na,ve,lo,this,is,the,word");
		input = new Input("iat\n" +
				"i,love,china,ch,na,ve,lo,this,is,the,word,beauti,tiful,ful");
		input = new Input("ilovechina,thewordisbeautiful\n" +
				"i,love,china,ch,na,ve,lo,this,is,the,word,beauti,tiful,ful");
		for (int i = 'a'; i <= 'z'; i++) {
			ArrayList<String> tokens = new ArrayList<>();
			tokens.add(((char)i) + "");
			map.put((char) i, tokens);
		}
	}

	public static void main(String[] args) {
		String ss = input.nextLine();
		String[] dict = input.nextLine().split(",");
		for (int i = 0; i < dict.length; i++) {
			List<String> strings = map.get(dict[i].charAt(0));
			strings.add(dict[i]);
			map.put(dict[i].charAt(0), strings);
		}
		for (Map.Entry<Character, List<String>> entry : map.entrySet()) {
			Collections.sort(entry.getValue(), (o1, o2) -> o2.length() - o1.length());
		}
		String[] words = ss.split("[^a-zA-Z]");
		List<String> result = new ArrayList<>();
		for (String word : words) {
			while (word.length() > 0) {
				List<String> tokens = map.get(word.charAt(0));
				for (String token : tokens) {
					if (word.startsWith(token)) {
						result.add(token);
						word = word.substring(token.length());
						break;
					}
				}

			}
		}
		System.out.println(String.join(",", result));
	}
}
