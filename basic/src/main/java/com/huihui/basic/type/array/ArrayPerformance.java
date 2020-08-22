package com.huihui.basic.type.array;

import java.util.Random;

/**
 * Created by pony on 2018/9/27.
 */
public class ArrayPerformance {
    public static void main(String[] args) {
        Random random = new Random();
        int[] ints = new int[100000000];
        for (int i = 0; i < 100000000; i++){
            ints[i] = random.nextInt(99999);
        }

        long t = System.currentTimeMillis();
        performance(100000, ints);
        System.out.println(System.currentTimeMillis() - t);
    }

    private static void performance(int jmax, int[] row) {

        IArray[] values = new IArray[jmax];

        for (int i = 0; i < row.length; i++){
            int index = row[i];
            if (values[index] == null){
                values[index] = new IArray();
            }
            values[index].add();
        }
        System.out.println(values[2].i);
    }

    private static class IArray{
        private int i = 0;
        public void add(){
            //i++;
        }
    }
}
