package com.winphysoft.basic.io.file;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/5/18
 */
public class FileChannelAppend {
    public static void main(String[] args) throws Exception{
        write();
        append();
        read();
    }

    private static void read() throws Exception{
        FileChannel fc = new RandomAccessFile("C:\\codes\\data\\append.log", "rw").getChannel();
        ByteBuffer allocate = ByteBuffer.allocate(20);
        fc.read(allocate);
        System.out.println(allocate.get(0));
        fc.close();
    }

    private static void append() throws Exception{
        FileChannel fc = new RandomAccessFile("C:\\codes\\data\\append.log", "rw").getChannel();
        ByteBuffer allocate = ByteBuffer.allocate(10);
        allocate.put((byte) 1);
        allocate.flip();
        fc.position(10);
        fc.write(allocate);
        fc.close();
    }

    private static void write() throws Exception{
        FileChannel fc = new RandomAccessFile("C:\\codes\\data\\append.log", "rw").getChannel();
        ByteBuffer allocate = ByteBuffer.allocate(10);
        allocate.put((byte) 1);
        allocate.flip();
        fc.write(allocate);
        fc.close();
    }
}
