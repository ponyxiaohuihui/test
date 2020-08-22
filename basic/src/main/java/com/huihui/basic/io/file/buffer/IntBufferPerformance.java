package com.huihui.basic.io.file.buffer;

import java.nio.IntBuffer;

public class IntBufferPerformance {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(100000000);
        for (int i = 0; i < 100000000; i++) {
            buffer.put(i);
        }
        while (true){
            performace(buffer);
        }
    }

    private static void performace(IntBuffer buffer) {
        long t = System.currentTimeMillis();
        int s = 0;
        for (int i = 0; i < 100000000; i++) {
            s += buffer.get(i);
        }
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(s);
    }
}
