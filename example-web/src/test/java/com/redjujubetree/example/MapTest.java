package com.redjujubetree.example;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {

	public static void main(String[] args) {
		HashMap<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.get("key1");
		ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
		concurrentHashMap.put("key1", "value1");
		concurrentHashMap.put("key2", "value2");
		concurrentHashMap.get("key1");
	}
}
