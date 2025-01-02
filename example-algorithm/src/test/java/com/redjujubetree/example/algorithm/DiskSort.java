package com.redjujubetree.example.algorithm;

import java.util.*;

public class DiskSort {
	static Input input;
	static {
		input = new Input("3\n1G\n" +
				"1024M\n" +
				"2G");
	}

	public static void main(String[] args) {
		int count = Integer.parseInt(input.nextLine());
		List<Disk> disks = new ArrayList<>() ;
		for (int i = 0; i < count; i++) {
			disks.add(new Disk(input.nextLine()));
		}
		Collections.sort(disks);
		for (Disk disk : disks) {
			System.out.println(disk.stringSize);
		}
	}

	static class Disk implements Comparable<Disk>{
		static Map<String, Long> map = new HashMap<>();
		static {
			map.put("G", 1024L);
			map.put("T", 1024 * 1024L);
			map.put("M", 1l);
		}
		public long size;
		public String stringSize;
		public Disk(String line) {
			this.stringSize = line;
			List<String> result = new ArrayList<>();
			String ss = "0" + stringSize;
			StringBuilder builder = new StringBuilder();
			for (int i = 1; i < ss.length(); i++) {
				builder.append(ss.charAt(i));
				if (Character.isDigit(ss.charAt(i))) {
					if (!Character.isDigit(ss.charAt(i-1))) {
						result.add(builder.toString());
						builder.setLength(0);
					}
				} else {
					if (Character.isDigit(ss.charAt(i-1))) {
						result.add(builder.toString());
						builder.setLength(0);
					}
				}
			}
			result.add(builder.toString());

			for (int i = 2; i < result.size(); i += 2) {
				this.size += map.get(result.get(i)) * Long.parseLong(result.get(i - 1));
			}

		}

		public int compareTo(Disk o) {
			if (this.size - o.size < 0) {
				return -1;
			}
			return 1;
		}
	}
}
