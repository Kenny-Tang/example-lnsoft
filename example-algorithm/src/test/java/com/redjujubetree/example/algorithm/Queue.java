package com.redjujubetree.example.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Queue {

	Input input;
	@BeforeEach
	public void setUp() throws Exception {
		input = new Input("4 1 3 5 2");
	}

	@Test
	public void queue() {
		try {

			String[] strs = input.nextLine().split(" ");
			int[] numbers = new int[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numbers[i] = Integer.parseInt(strs[i]);
			}

			int queueLen = numbers.length;
			boolean direction = true;
			for (int i = 0; i < queueLen - 1; i++, direction = !direction) {
				if (numbers[i] == numbers[i + 1] || numbers[i] > numbers[i + 1] == direction) {
					continue;
				}
				swap(numbers, i, i + 1);
			}

			for (int i = 0; i < queueLen; i++) {
				System.out.print(numbers[i] + " ");
			}
		} catch (Exception e) {
			System.out.println("[]");
		}
	}

	public void swap(int[] numbers , int i, int j) {
		int temp = numbers[i];
		numbers[i] = numbers[i+1];
		numbers[i+1] = temp;
	}
}
