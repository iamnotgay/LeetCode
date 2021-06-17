package com.github.iamnotgay.leetcode;

/**
 * @author: sanmao
 * @Date: 2021-06-16 23:36
 * @Version: 1.0
 *  素数个数统计
 *  des: 统计n以内的素数个数
 *  素数： 只能被1和自身整除的自然数，0，1除外
 *
 *  考点： 埃筛法
 */
public class Prime {

    //暴力算法
    public static int bf(int n){
        int count = 0;
        for (int i = 2; i < n; i++) {
          //判断是否为素数
            count +=isPrime(i)?1:0;
        }
        return count;
    }

    /* TODO isPrime */
    private static boolean isPrime(int x){
        for (int i = 2; i * i <= x; i++) {
          // 判断是否可以整除
            if (x % i == 0){
                return false;
            }
        }
        return true;
    }

    /* TODO 埃筛法*/
    public static int eratosthenes(int n){
        boolean[] isPrime = new boolean[n];
        int count = 0;
        for (int i = 2; i < n; i++) {
          //
            if (!isPrime[i]){
                count ++;
                for (int j=2 * i; j < n;j+=i ){
                    isPrime[j]=true;
                }
            }
        }
        return count;
    }

  public static void main(String[] args) {
    //
    System.out.println(bf(100));
    System.out.println(eratosthenes(100));
  }
}
