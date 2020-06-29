package com.winphysoft.basic.io.file.buffer;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/10/28
 */
public class DirectBufferTest {
    public static void main(String[] args) {
        LinkedList<ByteBuffer> ref = new LinkedList<>();
        new Thread(){
            @Override
            public void run() {
                while (true){
                    synchronized (ref){
                        try {
                            ref.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("pull one");
                    final ByteBuffer buf = ref.poll();
                    new Thread(){
                        @Override
                        public void run() {
                            int idx = 0;
                            int sum = 0;
                            while (idx++ < 9999){
                                sum += buf.get(idx);
                            }
                            System.out.println("sum is " + sum);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }

                        }


                    }.start();
                }
            }
        }.start();
        while(true){
            try{
                int idx = 0;
                ByteBuffer buf = ByteBuffer.allocateDirect(10000);
                while (idx++ < 10000){
                    buf.put((byte)idx);
                }
                System.out.println(buf.get(0));
                ref.add(buf);
            } catch (OutOfMemoryError e){
                synchronized (ref){
                    ref.notify();
                    System.out.println("notify one");
                }
            }
        }

    }
}
