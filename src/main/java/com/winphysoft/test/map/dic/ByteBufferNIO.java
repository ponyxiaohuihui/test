package com.winphysoft.test.map.dic;

import sun.misc.Unsafe;

import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/3/5
 */
public class ByteBufferNIO {
    private FileChannel fc;
    private long capacity;
    private MappedByteBuffer byteBuffer;

    public ByteBufferNIO(String path) {
        this(path, 1l << 10);
    }

    public ByteBufferNIO(String path, long capacity) {
        this.capacity = capacity;
        try {
            fc = new RandomAccessFile(path, "rw").getChannel();
            byteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, capacity);
        } catch (Exception e) {

        }
    }

    public void addString(String s){
        System.out.println(Platform.STRING_CHARARRAY_OFFSET);
        for (int i = 0; i < 40; i++) {
            System.out.println(Platform._UNSAFE.getByte(s, (long)i));
        }
        byteBuffer.put(s.getBytes());
    }

    public static void main(String[] args) throws Exception{
        ByteBufferNIO byteBufferNIO = new ByteBufferNIO("D:\\develop\\data\\buf");
        byteBufferNIO.addString("222");
    }
}
