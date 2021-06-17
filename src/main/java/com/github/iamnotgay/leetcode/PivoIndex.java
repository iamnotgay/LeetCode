package com.github.iamnotgay.leetcode;


import java.util.Arrays;

/**
 * @author: sanmao
 * @Date: 2021-06-17 22:56
 * @Version: 1.0
 * 数组中心下标
 */
public class PivoIndex {

    public static int privoIndex(int[] nums){
        int sum= Arrays.stream(nums).sum();
        int total = 0;

        for (int i = 0; i < nums.length; i++) {
         //
            total +=nums[i];
            if (total ==sum){
                return i;
            }
            sum -=nums[i];
        }
        return -1;
    }

  public static void main(String[] args) {
    //
    System.out.println(privoIndex(new int[]{1,7,3,6,5,6}));
  }
}
