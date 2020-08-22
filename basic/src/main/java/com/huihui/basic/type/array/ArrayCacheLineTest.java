package com.huihui.basic.type.array;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/4/19
 */
public class ArrayCacheLineTest {
    public static void main(String[] args) {
        printCache();
//        for (int i = 0; i < 26; i++) {
//            testPut(i);
//        }
     //   test(1<<8);
    }

    private static void printCache() {

        for (int j = 0; j < 20; j++) {
            int size = 1 << (j + 10);
            int[] arr = new int[size];
            long t = System.nanoTime();
            int lengthMod = arr.length - 1;
            int count = size << 4;
            for (int i = 0; i < count; i++)
            {
                arr[(i * 16) & lengthMod]++; // (x & lengthMod) is equal to (x % arr.Length)

            }
            System.out.println("size is " + (1<< j) + " k " + j +" cost " + ((System.nanoTime() - t) / count));
        }
    }

    private static void testPut(int col) {
        int row = 27 -col;
        int[][] ints = new int[1 << col][];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = new int[1 << row];
        }
        int colSize = 1<< col;
        int rowSize = 1<< row;
        long t = System.currentTimeMillis();
        int count = 10;
        while (count -- > 0){
            for (int i = 0; i < rowSize; i++) {
                for (int j = 0; j < colSize; j++) {
                    ints[j][i] = i;
                }
            }
        }
        System.out.println("col is : " + col + " col size : " +colSize + " cost " + ((System.currentTimeMillis() - t) / 10));
        t = System.currentTimeMillis();
    }

    private static void test(int range) {
        int[] ints = new int[1 << 27];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = i;
        }
        int s = 0;
        long t = System.currentTimeMillis();
        int div = ints.length / range;
        while (true){
            for (int j = 0; j < range; j++) {
                for (int i = 0; i < div; i++) {
                    s += ints[i * range + j];
                }
            }
            System.out.println(System.currentTimeMillis() - t);
            t = System.currentTimeMillis();
        }
    }
}
