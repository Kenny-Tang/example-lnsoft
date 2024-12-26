package com.redjujubetree.example.huawei;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Electric {

	int matrix[][] = null ;
	int squre;
	int lowerPower = 0;
	@BeforeEach
	public void beforeAll() {
		// 2 5 2 6
		matrix = new int[2+1][5+1];
		squre = 2;
		lowerPower = 6;

		String[] str1 = ("1 3 4 5 8 " +
						 "2 3 6 7 1").split(" ");
		int x = 0;
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix[0].length; j++) {
				matrix[i][j] = Integer.parseInt(str1[x++]);
			}
		}

	}

	@Test
	public void electricTest() {
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix[0].length; j++) {
				matrix[i][j] +=  matrix[i-1][j] + matrix[i][j-1] - matrix[i-1][j-1];
			}
		}
		int result = 0;
		for (int i = 1; i < matrix.length - squre + 1; i++) {
			for (int j = 1; j < matrix[0].length - squre + 1; j++) {
				int x = i + squre - 1;
				int y = j + squre - 1;
				int power = matrix[x][y] + matrix[i-1][j-1] - matrix[x][j-1] - matrix[i-1][y];
				if (power >= lowerPower) {
					result++;
				}
			}
		}
		System.out.println(result);
	}
}
