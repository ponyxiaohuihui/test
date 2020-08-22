package com.huihui.basic.vm;

/**
 * @author pony
 * Created by pony on 2020/8/6
 */
public class DeoptimizationTest {
    private DeoptimizationTest() {}
    static int someComplicatedFunction(double x)
    {
        return (int) (Math.pow(x, x) + Math.log(x) + Math.sqrt(x));
    }
    static void hotMethod(int iteration)
    {
        if (iteration < 20) {
            someComplicatedFunction(1.23);
        }
        else if (iteration < 40) {
            someComplicatedFunction(1.23);
        }
        else if (iteration < 60) {
            someComplicatedFunction(1.23);
        }
        else if (iteration < 80) {
            someComplicatedFunction(1.23);
        }
        else {
            someComplicatedFunction(1.23);
        }
    }
    static void hotMethodWrapper(int iteration)
    {
        int count = 100_000;
        for (int i = 0; i < count; i++) {
            hotMethod(iteration);
        }
    }
    public static void main(String[] args)
    {
        for (int k = 0; k < 100; k++) {
            long start = System.nanoTime();
            hotMethodWrapper(k + 1);
            System.out.println("iteration " + k + ": " + (System.nanoTime() - start) / 1_000_000 + "ms");
        }
        for (int k = 0; k < 100; k++) {
            long start = System.nanoTime();
            hotMethodWrapper(k + 1);
            System.out.println("iteration " + k + ": " + (System.nanoTime() - start) / 1_000_000 + "ms");
        }
    }
}
