package com.huihui.basic.thread.exhandel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ExceptionHandlerTest {
    public static void main(String[] args) throws Exception{
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(e);
            }
        });
        List list = new ArrayList<>();
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {

                    RandomAccessFile file = new RandomAccessFile("A.dat", "rw");
                    ByteBuffer buffer = ByteBuffer.allocate(100);
                    file.getChannel().write(buffer);
                } catch (Exception e){

                }
                throw new RuntimeException();
            }
        };
        thread.start();
        thread.join();
        System.out.println(list.get(0));
        System.out.println("success");
    }
}
