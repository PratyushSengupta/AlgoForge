import java.util.*;

class Solution {
    public String[] findRelativeRanks(int[] score) {

        int n = score.length;

        Integer[] index = new Integer[n];

        for (int i = 0; i < n; i++) {
            index[i] = i;
        }

        Arrays.sort(index, (a, b) -> score[b] - score[a]);

        String[] result = new String[n];

        for (int i = 0; i < n; i++) {

            int originalIndex = index[i];

            if (i == 0) {
                result[originalIndex] = "Gold Medal";
            } else if (i == 1) {
                result[originalIndex] = "Silver Medal";
            } else if (i == 2) {
                result[originalIndex] = "Bronze Medal";
            } else {
                result[originalIndex] = String.valueOf(i + 1);
            }
        }

        return result;
    }
}