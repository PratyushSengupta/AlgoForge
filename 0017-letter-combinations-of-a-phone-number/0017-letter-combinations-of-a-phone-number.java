import java.util.*;

class Solution {
    public List<String> letterCombinations(String digits) {

        List<String> result = new ArrayList<>();

        if (digits.length() == 0) {
            return result;
        }

        String[] map = {
            "", "", "abc", "def",
            "ghi", "jkl", "mno",
            "pqrs", "tuv", "wxyz"
        };

        backtrack(digits, 0, "", result, map);

        return result;
    }

    private void backtrack(String digits, int index, String current,
                            List<String> result, String[] map) {

        if (index == digits.length()) {
            result.add(current);
            return;
        }

        String letters = map[digits.charAt(index) - '0'];

        for (int i = 0; i < letters.length(); i++) {
            backtrack(
                digits,
                index + 1,
                current + letters.charAt(i),
                result,
                map
            );
        }
    }
}