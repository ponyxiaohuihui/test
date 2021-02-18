package com.huihui.aparapi;

import com.aparapi.Kernel;
import com.aparapi.Range;


/**
 * @author pony
 * Created by pony on 2020/11/20
 */
public class Test {
    static int  SIZE = 64 * 1024 * 1024;
    public static void main(String[] args) {
        final int a[] = new int[SIZE];
        final int b[] = new int[SIZE];
        final int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
                a[i] = i;
                b[i] = i;
        }
        runKernel(a, b, result);
        runCpu(a, b, result);
    }

    public static void runCpu(int[] a, int[] b, int[] result) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < result.length; j++) {
                    result[j] = a[j] + b[j];
            }
            System.out.println("java cpu cost " + (System.currentTimeMillis() - t) + " ms");
            t = System.currentTimeMillis();
        }
    }

    public static void runKernel(final int[] a, final int[] b, final int[] result) {
        long t = System.currentTimeMillis();
        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int i = getGlobalId();
                result[i] = a[i] + b[i];
            }
        };
        for (int i = 0; i < 5; i++) {
            Range range = Range.create(SIZE, 64);
            kernel.execute(range);
            System.out.println("java oc cost " + (System.currentTimeMillis() - t)  + " ms");
            t = System.currentTimeMillis();
        }
    }
}
