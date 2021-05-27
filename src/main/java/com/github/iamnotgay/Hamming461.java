package com.github.iamnotgay;

/**
 * @author: sanmao @Date: 2021-05-27 14:07 @Version: 1.0
 *     <p>两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
 *     <p>给出两个整数 x 和 y，计算它们之间的汉明距离。 注意： 0 ≤ x, y < 231.
 */
public class Hamming461 {

    /**
     * 内置位计数功能
     * @param x
     * @param y
     * @return
     */
    public int hammingDistance(int x,int y){

        return Integer.bitCount(x^y);
    }


    /**
     * 位移实现计数
     * @param x
     * @param y
     * @return
     */
    public int hammingDistance2(int x,int y){

        int s= x^y , ret=0;

        while (s!=0){
            ret += s&1;
            s >>=1;
        }
        return ret;
    }

  public static void main(String[] args) {
    //
      Hamming461 hamming461 = new Hamming461();
      System.out.println(hamming461.hammingDistance2(1,3));
  }


}
