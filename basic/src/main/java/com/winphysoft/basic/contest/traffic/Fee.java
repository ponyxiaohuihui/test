package com.winphysoft.basic.contest.traffic;

import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.PrimitiveIterator;
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
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\6.in", "rw");
        FileChannel fc = file.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 80000004);
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(10000000);
        PrimitiveIterator.OfInt it = random.ints(9900000, 0, 10000000).iterator();
        PrimitiveIterator.OfInt fee = random.ints(9900000, 0, 100000000).iterator();
        for (int i = 0; i < 9900000; i++){
            intBuffer.put(it.next() * 139);
            intBuffer.put(fee.next());
        }
        for (int j = 0; j < 10; j ++){
            for (int i = 0; i < 10000; i++){
                intBuffer.put(i * 135791);
                intBuffer.put(i*j*10);
            }
        }
        buffer.clear();
        fc.close();
    }
}
