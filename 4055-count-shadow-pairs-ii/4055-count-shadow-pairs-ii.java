import java.util.*;

class Solution {

    int[] nums;
    int[] sorted;
    long answer;

    class Fenwick {
        int[] tree;

        Fenwick(int n) {
            tree = new int[n + 1];
        }

        void add(int index, int value) {
            index++;

            while (index < tree.length) {
                tree[index] += value;
                index += index & -index;
            }
        }

        int sum(int index) {
            if (index < 0) {
                return 0;
            }

            index++;

            int result = 0;

            while (index > 0) {
                result += tree[index];
                index -= index & -index;
            }

            return result;
        }

        int rangeSum(int left, int right) {
            if (left > right) {
                return 0;
            }

            return sum(right) - sum(left - 1);
        }
    }

    public int shadowPairs(int[] nums) {
        this.nums = nums;

        int n = nums.length;

        sorted = nums.clone();
        Arrays.sort(sorted);

        int m = 0;

        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sorted[m++] = sorted[i];
            }
        }

        sorted = Arrays.copyOf(sorted, m);

        answer = 0;

        divide(0, n - 1);

        return (int) answer;
    }

    private void divide(int left, int right) {

        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;

        divide(left, mid);
        divide(mid + 1, right);

        countCross(left, mid, right);
    }

    private void countCross(int left, int mid, int right) {

        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        /*
         * b[i] =
         * smallest value > nums[i] appearing after i
         * in the left half.
         */
        int[] b = new int[leftSize];

        TreeSet<Integer> set = new TreeSet<>();

        for (int i = mid; i >= left; i--) {

            Integer higher = set.higher(nums[i]);

            if (higher == null) {
                b[i - left] = Integer.MAX_VALUE;
            } else {
                b[i - left] = higher;
            }

            set.add(nums[i]);
        }

        /*
         * c[j] =
         * largest value < nums[j] appearing before j
         * in the right half.
         */
        int[] c = new int[rightSize];

        set.clear();

        for (int j = mid + 1; j <= right; j++) {

            Integer lower = set.lower(nums[j]);

            if (lower == null) {
                c[j - mid - 1] = Integer.MIN_VALUE;
            } else {
                c[j - mid - 1] = lower;
            }

            set.add(nums[j]);
        }

        /*
         * Sort left indices by b[i] descending.
         */
        Integer[] leftIndices = new Integer[leftSize];

        for (int i = 0; i < leftSize; i++) {
            leftIndices[i] = left + i;
        }

        Arrays.sort(leftIndices, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer d) {

                int ba = b[a - left];
                int bd = b[d - left];

                if (ba != bd) {
                    return Integer.compare(bd, ba);
                }

                return Integer.compare(nums[a], nums[d]);
            }
        });

        /*
         * IMPORTANT:
         * Process right endpoints by nums[j] DESCENDING.
         *
         * This allows us to only ADD elements to the Fenwick tree.
         */
        Integer[] rightIndices = new Integer[rightSize];

        for (int i = 0; i < rightSize; i++) {
            rightIndices[i] = mid + 1 + i;
        }

        Arrays.sort(rightIndices, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer d) {

                if (nums[a] != nums[d]) {
                    return Integer.compare(nums[d], nums[a]);
                }

                return Integer.compare(a, d);
            }
        });

        Fenwick fenwick = new Fenwick(sorted.length);

        int pointer = 0;

        for (int x = 0; x < rightSize; x++) {

            int j = rightIndices[x];
            int value = nums[j];

            /*
             * Add every left index satisfying:
             *
             * nums[j] <= b[i]
             */
            while (pointer < leftSize) {

                int i = leftIndices[pointer];

                if (b[i - left] < value) {
                    break;
                }

                int position = lowerBound(sorted, nums[i]);

                fenwick.add(position, 1);

                pointer++;
            }

            /*
             * We need:
             *
             * c[j] <= nums[i] < nums[j]
             */

            int low = lowerBound(
                sorted,
                c[j - mid - 1]
            );

            int high = lowerBound(
                sorted,
                value
            ) - 1;

            if (low <= high) {
                answer += fenwick.rangeSum(low, high);
            }
        }
    }

    private int lowerBound(int[] arr, int target) {

        int left = 0;
        int right = arr.length;

        while (left < right) {

            int mid = left + (right - left) / 2;

            if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }
}