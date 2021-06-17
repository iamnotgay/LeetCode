package com.github.iamnotgay.leetcode;

/**
 * @author: sanmao
 * @Date: 2021-06-17 22:39
 * @Version: 1.0
 * 删除数组中重复元素,统计去重后数组元素个数
 * 双指针
 */
public class RemoveDuplicates {


    public static int removeDuplicates(int[] nums){
        if (nums.length==0){
            return 0;
        }
        int i=0;
        for (int j = 1; j < nums.length; j++) {
          //
            if (nums[j]!=nums[i]){
                i++;
                nums[i]=nums[j];
            }
        }
        return i;
    }

  public static void main(String[] args) {
    //
    System.out.println(removeDuplicates(new int[]{0,1,1,2,3,4,4,5}));
  }
}
