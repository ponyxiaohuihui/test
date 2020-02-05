package com.winphysoft.test.loop;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/1/16
 */
public class LoopPlusPerformance {
    public static void main(String[] args) {
        int i = 10;
        while (i-->0){
            test1();
            test2();
        }
    }

    private static void test1() {
        int[] values = new int[1000000000];
        long t = System.currentTimeMillis();
        int i = 0;
        while (i < 1000000000){
            values[i++] = i;
        }
        System.out.println(System.currentTimeMillis() - t);
    }
    private static void test2() {
        int[] values = new int[1000000000];
        long t = System.currentTimeMillis();
        int i = 0;
        while (i < 1000000000){
            values[i] = i;
            i++;
        }
        System.out.println(System.currentTimeMillis() - t);
    }
}
