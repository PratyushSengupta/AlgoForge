class Solution {
    public String removeOuterParentheses(String s) {
        StringBuilder result = new StringBuilder();
        int depth = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                if (depth > 0) {
                    result.append('(');
                }
                depth++;
            } else {
                depth--;
                if (depth > 0) {
                    result.append(')');
                }
            }
        }

        return result.toString();
    }
}