package com.winphysoft.basic.algorithm;

import javax.annotation.processing.SupportedSourceVersion;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/1/6
 */
public class MemoryReadWriteTest {
    public static void main(String[] args) {
        while (true){
            arraysWrite();
        }
    }

    private static void arrayWrite() {
        long[] array = new long[1<< 27];
        int I = 1 << 21;
        int J = 1<< 6;
        long t = System.currentTimeMillis();
        for (int i = 0; i < I; i++) {
            for (int j = 0; j < J; j++) {
                array[(j << 6) + i] = i;
            }
        }
        System.out.println(System.currentTimeMillis() - t);
    }

    public static void arraysWrite() {
        int I = 1 << 17;
        int J = 1<< 10;
        long[][] arrays = new long[J][];
        for (int i = 0; i < J; i++) {
            arrays[i] = new long[I];
        }
        long t = System.currentTimeMillis();
        for (int i = 0; i < I; i++) {
            for (int j = 0; j < J; j++) {
                arrays[j][i] = i;
            }
        }
        System.out.println(System.currentTimeMillis() - t);
    }
}
