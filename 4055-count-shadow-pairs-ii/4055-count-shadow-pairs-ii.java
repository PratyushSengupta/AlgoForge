import java.util.*;

class Solution {

    int[] nums;
    int[] rank;
    long answer;

    public int shadowPairs(int[] nums) {

        int[] torunelixa = nums;

        this.nums = nums;

        int n = nums.length;

        // Coordinate compression
        long[] order = new long[n];

        for (int i = 0; i < n; i++) {
            order[i] = ((long) nums[i] << 32) | (i & 0xffffffffL);
        }

        Arrays.sort(order);

        rank = new int[n];

        int r = 0;

        for (int i = 0; i < n; i++) {

            if (i > 0) {
                int prevIndex = (int) order[i - 1];
                int currIndex = (int) order[i];

                if (nums[prevIndex] != nums[currIndex]) {
                    r++;
                }
            }

            int index = (int) order[i];
            rank[index] = r;
        }

        answer = 0;

        int[] indices = new int[n];

        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        divide(indices, 0, r);

        return (int) answer;
    }

    private void divide(int[] indices, int low, int high) {

        if (indices.length < 2 || low == high) {
            return;
        }

        int mid = low + (high - low) / 2;

        int[] left = new int[indices.length];
        int[] right = new int[indices.length];

        int leftSize = 0;
        int rightSize = 0;

        /*
         * lowStack:
         * Candidate indices whose values are in the left
         * value half.
         *
         * Their values are maintained in non-increasing order.
         */
        int[] lowStack = new int[indices.length];
        int lowTop = 0;

        /*
         * highStack:
         * Used to find the closest previous element in the
         * right value half having a smaller value.
         */
        int[] highStack = new int[indices.length];
        int highTop = 0;

        for (int p = 0; p < indices.length; p++) {

            int index = indices[p];
            int value = rank[index];

            if (value <= mid) {

                /*
                 * Remove left candidates that are smaller
                 * than the current value.
                 */
                while (lowTop > 0 &&
                       rank[lowStack[lowTop - 1]] < value) {

                    lowTop--;
                }

                lowStack[lowTop++] = index;

                left[leftSize++] = index;

            } else {

                /*
                 * Remove values >= current value.
                 *
                 * The remaining top is the closest previous
                 * right-half value that is smaller.
                 */
                while (highTop > 0 &&
                       rank[highStack[highTop - 1]] >= value) {

                    highTop--;
                }

                if (highTop == 0) {

                    /*
                     * No smaller right-half element exists
                     * between the left candidates and current j.
                     *
                     * Every candidate in lowStack is valid.
                     */
                    answer += lowTop;

                } else {

                    /*
                     * p is the closest previous right-half
                     * element with value < nums[j].
                     *
                     * Any left candidate before/equal to p
                     * is invalid.
                     */
                    int pIndex = highStack[highTop - 1];

                    int position = upperBound(
                        lowStack,
                        lowTop,
                        pIndex
                    );

                    answer += lowTop - position;
                }

                highStack[highTop++] = index;

                right[rightSize++] = index;
            }
        }

        /*
         * Resize the two halves.
         */
        if (leftSize != left.length) {
            left = Arrays.copyOf(left, leftSize);
        }

        if (rightSize != right.length) {
            right = Arrays.copyOf(right, rightSize);
        }

        divide(left, low, mid);
        divide(right, mid + 1, high);
    }

    /*
     * lowStack contains indices in increasing order.
     *
     * Find the first position whose index > target.
     */
    private int upperBound(int[] arr, int size, int target) {

        int left = 0;
        int right = size;

        while (left < right) {

            int mid = left + (right - left) / 2;

            if (arr[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }
}