class Solution {
    public boolean hasValidPath(char[][] grid) {

        int m = grid.length;
        int n = grid[0].length;

        // Path length must be even
        if ((m + n - 1) % 2 != 0) {
            return false;
        }

        // Maximum possible balance
        int maxBalance = m + n;

        boolean[][][] dp = new boolean[m][n][maxBalance];

        // First character must be '('
        if (grid[0][0] == ')') {
            return false;
        }

        dp[0][0][1] = true;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (i == 0 && j == 0) {
                    continue;
                }

                int change;

                if (grid[i][j] == '(') {
                    change = 1;
                } else {
                    change = -1;
                }

                for (int balance = 0; balance < maxBalance; balance++) {

                    int previousBalance = balance - change;

                    if (previousBalance < 0 ||
                        previousBalance >= maxBalance) {
                        continue;
                    }

                    boolean possible = false;

                    // From top
                    if (i > 0 && dp[i - 1][j][previousBalance]) {
                        possible = true;
                    }

                    // From left
                    if (j > 0 && dp[i][j - 1][previousBalance]) {
                        possible = true;
                    }

                    dp[i][j][balance] = possible;
                }
            }
        }

        return dp[m - 1][n - 1][0];
    }
}