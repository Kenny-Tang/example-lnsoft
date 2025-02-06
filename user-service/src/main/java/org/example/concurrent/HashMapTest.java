package org.example.concurrent;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("11", "11");
        map.put("22", "22");
        map.put("33", "33");
        map.put("44", "44");

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("11", "11");
        hashMap.put("22", "22");
        hashMap.put("33", "33");
        hashMap.put("44", "44");

        String s = hashMap.get("11");
    }
}
