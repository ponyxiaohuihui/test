package com.winphysoft.test.map.dic;

import sun.misc.Unsafe;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * @author pony
 * @version 1.1
 * 直接用NIO写String的bits
 * Created by pony on 2020/3/5
 */
public class ByteBufferNIO {
    private FileChannel fc;
    private int maxSize;
    private int size;
    private CharBuffer charBuffer;

    public ByteBufferNIO(String path) {
        this(path, Integer.MAX_VALUE);
    }

    public ByteBufferNIO(String path, int capacity) {
        try {
            fc = new RandomAccessFile(path, "rw").getChannel();
            charBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, capacity).asCharBuffer();
            maxSize = charBuffer.capacity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addString(String s) {
        char[] stringChars = (char[]) Platform._UNSAFE.getObject(s, Platform.STRING_CHARARRAY_OFFSET);
        charBuffer.put(stringChars);
    }

    public char getChar(int idx) {
        return charBuffer.get(idx);
    }

//    private void checkSize(int length) {
//        int lastSize = size;
//        size += length;
//        if (size > maxSize) {
//            int capacity = (size >> 21) + 1 << 21;
//            try {
//                charBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, capacity).asCharBuffer();
//                charBuffer.position(lastSize);
//                maxSize = charBuffer.capacity();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static void main(String[] args) throws Exception {
        ByteBufferNIO byteBufferNIO = new ByteBufferNIO("C:\\code\\buf");
        int idx = 0;
        long t = System.currentTimeMillis();
        while (idx++ < 10000000){
            byteBufferNIO.addString(UUID.randomUUID().toString());
        }
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        idx = 0;
        int s = 0;
        while (idx++ < 10000000 * 16){
            s += byteBufferNIO.getChar(idx);
        }
        System.out.println(s);
        System.out.println(System.currentTimeMillis() - t);
    }
}
