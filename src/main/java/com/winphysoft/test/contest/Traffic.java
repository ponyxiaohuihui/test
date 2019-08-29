package com.winphysoft.test.contest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class Traffic {
    public static void main(String[] args) throws Exception {
        write();
    }

    public static void readNio() throws Exception{
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\1.in", "rw");
        FileChannel fc = file.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 48);
        IntBuffer intBuffer = buffer.asIntBuffer();
    }

    public static void read() throws Exception {
        FilterInputStream bufferedInputStream = (FilterInputStream) System.in;
        Field f = FilterInputStream.class.getDeclaredField("in");
        f.setAccessible(true);
        FileInputStream fs = (FileInputStream) f.get(bufferedInputStream);
        FileChannel fc = fs.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        IntBuffer intBuffer = buffer.asIntBuffer();
    }

    public static void readOb() throws Exception {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\codes\\out.out")));
        System.out.println(in.readInt());
        System.out.println(in.readInt());
    }

    public static void write() throws Exception {
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\6.in", "rw");
        FileChannel fc = file.getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 40000008);
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(10000000);
        intBuffer.put(10000);
        Random random = new Random();
        for (int i = 0; i < 8500000; i++){
            intBuffer.put(random.nextInt(20000000) * 11);
        }
        for (int i = 0; i < 600000; i++){
            intBuffer.put(9992123);
        }
        for (int i = 0; i < 900000; i++){
            intBuffer.put(random.nextInt(30000) * 732);
        }
        buffer.clear();
        fc.close();
    }
    public static void writeAns() throws Exception {
        System.setOut(new PrintStream("C:\\codes\\1.out"));
        System.out.println(5);
        System.out.println(7/3);
    }
}
