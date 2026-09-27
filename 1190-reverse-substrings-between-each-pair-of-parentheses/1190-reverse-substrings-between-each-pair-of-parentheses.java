class Solution {
    public String reverseParentheses(String s) {
        Stack<String> stack = new Stack<>();
        String current = "";

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (ch == '(') {
                stack.push(current);
                current = "";
            } 
            else if (ch == ')') {
                current = new StringBuilder(current).reverse().toString();
                current = stack.pop() + current;
            } 
            else {
                current += ch;
            }
        }

        return current;
    }
}