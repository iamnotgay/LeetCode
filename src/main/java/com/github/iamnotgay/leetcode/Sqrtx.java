package com.github.iamnotgay.leetcode;

/**
 * @author: sanmao
 * @Date: 2021-06-17 23:31
 * @Version: 1.0
 */
public class Sqrtx {

    //二分查找
    public static int binarySearch(int x){
        int index = -1,l=0,r=x;

        while (l <= r){
            int mid = l+(r-l)/2;
            if (mid*mid <=x){
                index = mid;
                l = mid+1;
            }else {
                r= mid-1;
            }
        }
        return index;
    }

  public static void main(String[] args) {
    //
    System.out.println(binarySearch(24));
  }
}
