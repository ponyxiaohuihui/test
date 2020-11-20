package com.huihui.aparapi;

import com.aparapi.Kernel;
import com.aparapi.Range;

import java.util.Random;

/**
 * @author pony
 * Created by pony on 2020/11/20
 */
public class Test {
    public static void main(String[] args) {
        final float a[] = new float[100000000];
        final float b[] = new float[100000000];
        final float[] result = new float[a.length];
        Random rand = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = rand.nextFloat();
            b[i] = rand.nextFloat();
        }
        //runKernel(a, b, result);
        runCpu(a, b, result);
    }

    public static void runCpu(float[] a, float[] b, float[] result) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < result.length; j++) {
                result[j] = a[j] + b[j];
            }
            System.out.println(result[0]);
            System.out.println("cost " + (System.currentTimeMillis() - t));
            t = System.currentTimeMillis();
        }
    }

    public static void runKernel(final float[] a, final float[] b, final float[] result) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Kernel kernel = new Kernel() {
                @Override
                public void run() {
                    int i = getGlobalId();
                    result[i] = a[i] + b[i];
                }
            };
            Range range = Range.create(result.length);
            kernel.execute(range);
            System.out.println(result[0]);
            System.out.println("cost " + (System.currentTimeMillis() - t));
            t = System.currentTimeMillis();
        }
    }
}
