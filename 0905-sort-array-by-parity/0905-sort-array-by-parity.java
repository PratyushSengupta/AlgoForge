class Solution {
    public int[] sortArrayByParity(int[] nums) {
        int [] nums1 = new int [nums.length];
        int count = 0;
        for(int i = 0; i<nums.length; i++){
            if(nums[i] % 2 == 0){
                nums1[count++] = nums[i];
                //nums1[i] = nums[i];
            }
        }

        for (int i= 0; i<nums.length;i++){
            if(nums[i] % 2 != 0){
                nums1[count++] = nums[i];
            }
        }
        return nums1;
    }
}