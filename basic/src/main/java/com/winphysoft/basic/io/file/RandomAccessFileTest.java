package com.winphysoft.basic.io.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class RandomAccessFileTest {
    public static void main(String[] args) throws IOException {
        testRelative();
        testAbsolute();
    }

    private static void testRelative() throws IOException{
        FileChannel fc = new RandomAccessFile("/cubes/table/seg0/column/byte/child_overwrite/0",  "rw").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10000000);
        buffer.put("bb".getBytes());
        fc.write(buffer);
        fc.close();
    }

    private static void testAbsolute() throws IOException{
        FileChannel fc = new RandomAccessFile("D://b//t4",  "rw").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10000000);
        buffer.put("bb".getBytes());
        fc.write(buffer);
        fc.close();
    }
}
