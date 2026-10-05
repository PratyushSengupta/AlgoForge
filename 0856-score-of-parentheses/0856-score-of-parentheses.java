import java.util.*;

class Solution {
    public int scoreOfParentheses(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(0);

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(0);
            } else {
                int value = stack.pop();

                if (value == 0) {
                    value = 1;
                } else {
                    value = 2 * value;
                }

                stack.push(stack.pop() + value);
            }
        }

        return stack.pop();
    }
}