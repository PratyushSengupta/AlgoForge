class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        int n = nums1.length;
        long k = (long) k1 + k2;

        long[] diff = new long[n];
        long max = 0;
        long total = 0;

        for (int i = 0; i < n; i++) {
            diff[i] = Math.abs((long) nums1[i] - nums2[i]);
            max = Math.max(max, diff[i]);
            total += diff[i];
        }

        if (total <= k) {
            return 0;
        }

        long left = 0;
        long right = max;

        while (left < right) {
            long mid = left + (right - left) / 2;
            long needed = 0;

            for (int i = 0; i < n; i++) {
                if (diff[i] > mid) {
                    needed += diff[i] - mid;
                }
            }

            if (needed <= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        long level = left;
        long used = 0;
        long answer = 0;

        for (int i = 0; i < n; i++) {
            if (diff[i] > level) {
                used += diff[i] - level;
                answer += level * level;
            } else {
                answer += diff[i] * diff[i];
            }
        }

        long remaining = k - used;

        // Reduce the remaining differences from level to level - 1.
        // Each such reduction costs 2 * level - 1 in squared difference.
        for (int i = 0; i < n && remaining > 0; i++) {
            if (diff[i] >= level && level > 0) {
                answer -= 2 * level - 1;
                remaining--;
            }
        }

        return answer;
    }
}