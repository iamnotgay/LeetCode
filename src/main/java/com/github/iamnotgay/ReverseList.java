package com.github.iamnotgay;

/**
 * @author: sanmao 链表反转
 * @Date: 2021-06-16 22:49
 * @Version: 1.0
 * 1 -> 2 -> 3 -> 4
 * null <- 1  <- 2 <- 3 <- 4
 *
 * prev curr next
 */
public class ReverseList {

    /* TODO 链表结构*/
    static class ListNode{
        int val;
        ListNode next;

        public ListNode(int val,ListNode next){
            this.val = val;
            this.next = next;
        }
    }

    /* TODO 迭代*/
    public static ListNode iterate(ListNode head){

        ListNode pre = null,next;
        ListNode curr = head;

        while (curr!=null){
            next = curr.next;
            curr.next = pre;
            pre  = curr;
            curr = next;
        }
        return pre;
    }

  public static void main(String[] args) {
    //初始化数据
      ListNode node5 = new ListNode(5,null);
      ListNode node4 = new ListNode(5,node5);
      ListNode node3 = new ListNode(5,node4);
      ListNode node2 = new ListNode(5,node3);
      ListNode node1 = new ListNode(5,node2);

      ListNode iterate = iterate(node1);
  }
}
