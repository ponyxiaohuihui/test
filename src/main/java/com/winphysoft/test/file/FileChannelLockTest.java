package com.winphysoft.test.file;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileChannelLockTest {
    public static void main(String[] args) throws Exception {
        FileChannel fileChannel = new RandomAccessFile("D://l.dat","rw").getChannel();
        MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, 1000);
        new Thread(){
            private boolean flag = true;
            @Override
            public void run() {
                while(flag){
                    FileLock lock = null;

                    try{
                        lock = fileChannel.tryLock();
                        try {
                            Thread.sleep(600l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(byteBuffer.get(0));
                        flag = false;
                    } catch (Exception e){
                        try {
                            Thread.sleep(100l);
                        } catch (Exception e1){
                        }
                    } finally {
                        try {
                            lock.release();
                        } catch (Exception e){
                        }
                    }
                }
            }
        }.start();
        new Thread(){
            private boolean flag = true;
            @Override
            public void run() {
                while(flag){
                    FileLock lock = null;
                    try {
                        lock = fileChannel.tryLock();
                        System.out.println("lock");
                        Thread.sleep(2000l);
                        System.out.println("release");
                        flag = false;
                    } catch (Exception e) {
                        try {
                            Thread.sleep(10l);
                        } catch (Exception e1){
                        }
                       // e.printStackTrace();
                    } finally {
                        try {
                            lock.release();
                        } catch (Exception e){
                        }
                    }
                }
            }
        }.start();
    }
}
