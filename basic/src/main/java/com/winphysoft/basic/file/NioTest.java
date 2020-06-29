package com.winphysoft.basic.file;

import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class NioTest {
    public static void main(String[] args) throws Exception{
        long t = System.currentTimeMillis();
        read("0");
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        read("0");
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        read("0");
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        read("0");
        System.out.println(System.currentTimeMillis() - t);
    }

    private static void read(String name) throws Exception {
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\" + name, "r");
        FileChannel fc = file.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, 1600000000);
        LongBuffer longBuffer = buffer.asLongBuffer();
        int sum = 0;
        for (int i = 0; i < 200000000; i++){
            sum += longBuffer.get();
        }
        System.out.println(sum);
    }

    private static void write(String name) throws Exception {
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\" + name, "rw");
        FileChannel fc = file.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1600000000);
        LongBuffer intBuffer = buffer.asLongBuffer();
        Random random = new Random();
        for (int i = 0; i < 200000000; i++){
            intBuffer.put(random.nextLong());
        }
        buffer.clear();
        fc.close();
    }
}
