class Solution {
    public int minCost(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int bits = 1024;

        boolean[][] prev = new boolean[n][bits];
        boolean[][] curr = new boolean[n][bits];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int x = 0; x < bits; x++) {
                    curr[j][x] = false;
                }

                int value = grid[i][j];

                if (i == 0 && j == 0) {
                    curr[j][value] = true;
                    continue;
                }

                if (i > 0) {
                    for (int x = 0; x < bits; x++) {
                        if (prev[j][x]) {
                            curr[j][x ^ value] = true;
                        }
                    }
                }

                if (j > 0) {
                    for (int x = 0; x < bits; x++) {
                        if (curr[j - 1][x]) {
                            curr[j][x ^ value] = true;
                        }
                    }
                }
            }

            boolean[][] temp = prev;
            prev = curr;
            curr = temp;
        }

        for (int x = 0; x < bits; x++) {
            if (prev[n - 1][x]) {
                return x;
            }
        }

        return -1;
    }
}