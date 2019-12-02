package com.winphysoft.test.shift;

import java.util.Random;

/**
 * Created by 小灰灰 on 2016/8/5.
 */
public class SiftTest {
    public static void main(String[] args) {
        int[] ints = new int[100000000];
        Random random = new Random();
        for (int i = 0; i < ints.length; i++){
            ints[i] = random.nextInt();
        }
        long t = System.currentTimeMillis();
        int j = 0;
        for (int i :ints){
            j =i%3;
        }
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        for (int i :ints){
            j =i%2;
        }
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        for (int i :ints){
            j =i<<2;
        }
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(j);
     //   System.out.println(32000<<6);
    }
}
