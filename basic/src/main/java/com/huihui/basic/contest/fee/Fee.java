package com.huihui.basic.contest.fee;

import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class Fee {
    public static void main(String[] args) throws Exception {
        write();
        //read();
    }

    public static void read() throws Exception{
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\4.in", "rw");
        FileChannel fc = file.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 80000004);
        IntBuffer intBuffer = buffer.asIntBuffer();
        for (int i = 0; i < 101; i++) {
            System.out.println(intBuffer.get(i));
        }
        buffer.clear();
        fc.close();
    }


    public static void write() throws Exception {
        Random random = new Random();
        random.ints().iterator();
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\10.in", "rw");
        FileChannel fc = file.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 80000004);
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(10000000);
        for (int i = 0; i < 100000; i++){
            for (int j = 0; j < 100; j++) {
                intBuffer.put(j + i * 10000);
                intBuffer.put(j + i * 10000);
            }
        }
        buffer.force();
        buffer.clear();

        fc.close();
    }
}
