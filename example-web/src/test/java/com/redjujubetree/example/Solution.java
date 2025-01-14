package com.redjujubetree.example;

public class Solution {

    int threshold = 0;
    int r = 0;
    int c = 0;
    int count = 0;
    int[][] mat;

    public static void main(String[] args) {
        new Solution().movingCount(1,2,3);
    }
    public int movingCount(int threshold, int rows, int cols) {
        this.threshold = threshold;
        this.r = rows;
        this.c = cols;
        mat = new int[rows][cols];
        dfs(0,0);
        return count;

    }
    
    public void dfs(int x, int y) {
        if (x < 0) return;
        if (y < 0) return;
        if (x == r) return;
        if (y == c) return;
        if (mat[x][y] > 0) {
            return ;
        }
        mat[x][y]++;
        if (!touchThreshold(x, y)) {
            count++;
        } else {
            return;
        }
        dfs(x + 1, y);
        dfs(x, y + 1);
    }

    public boolean touchThreshold(int x, int y) {
        int total = 0;
        while (x > 0) {
            total += x % 10;
            x /= 10;
        }
        while (y > 0) {
            total += y % 10;
            y /= 10;
        }
        return total > threshold;
    }
}
