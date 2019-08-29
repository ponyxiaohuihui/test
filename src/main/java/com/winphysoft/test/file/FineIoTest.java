package com.winphysoft.test.file;

import com.fineio.FineIO;
import com.fineio.io.file.IOFile;
import com.fr.swift.cube.io.impl.fineio.connector.Lz4Connector;

import java.net.URI;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class FineIoTest {
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
    }

    private static void read(String name) throws Exception {
        Lz4Connector connector = Lz4Connector.newInstance("C:\\codes\\fineio");
        AtomicBoolean flag = new AtomicBoolean(false);
        IOFile<com.fineio.io.IntBuffer> ioFile = FineIO.createIOFile(connector, URI.create(name), FineIO.MODEL.READ_INT, true);
        int sum = 0;
        for (int i = 0; i < 200000000; i++){
            flag.compareAndSet(false, false);
            sum += FineIO.getInt(ioFile,i);
        }
        System.out.println(sum);
    }

    private static void write(String name) throws Exception {
        Lz4Connector connector = Lz4Connector.newInstance("C:\\codes\\fineio");
        IOFile<com.fineio.io.IntBuffer> ioFile = FineIO.createIOFile(connector, URI.create(name), FineIO.MODEL.WRITE_INT, true);
        Random random = new Random();
        for (long i = 0; i < 200000000; i++){
            FineIO.put(ioFile, i, random.nextInt(200000000));
        }
        ioFile.close();
    }
}
