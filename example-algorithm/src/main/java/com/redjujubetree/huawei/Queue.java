package com.redjujubetree.huawei;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 华为OD机试真题-高矮个子排队-2024年OD统一考试（E卷）
 */
public class Queue {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			String[] queue = scanner.nextLine().split(" ");
			int[] array = new int[queue.length];
			for (int i = 0; i < array.length; i++) {
				array[i] = Integer.parseInt(queue[i]);
			}
			boolean flag = true; // 默认使用正向排队
			for (int i = 0; i < array.length - 1; i++) {
				if (flag) {
					if (array[i] < array[i + 1]) {
						swap(array, i, i + 1);
					}
				} else {
					if (array[i] > array[i + 1]) {
						swap(array,i, i + 1);
					}
				}
				flag = !flag;
			}
			List<String> collect = Arrays.stream(array).mapToObj(x -> x + "").collect(Collectors.toList());
			String join = String.join(" ", collect);
			System.out.println(join);
		} catch (Exception e) {
			System.out.println("[]");
		}
	}
	public static void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}
