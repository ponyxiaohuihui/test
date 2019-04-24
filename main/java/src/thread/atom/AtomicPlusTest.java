package thread.atom;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicPlusTest {
    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        new Thread(){
            public void run(){
                while (atomicInteger.incrementAndGet() < 100000000){

                };
                System.out.println(System.currentTimeMillis() - t);
            }
        }.start();
        new Thread(){
            public void run(){
                while (atomicInteger.incrementAndGet() < 100000000){

                };
                System.out.println(System.currentTimeMillis() - t);
            }
        }.start();
        new Thread(){
            public void run(){
                while (atomicInteger.incrementAndGet() < 100000000){

                };
                System.out.println(System.currentTimeMillis() - t);
            }
        }.start();
        new Thread(){
            public void run(){
                while (atomicInteger.incrementAndGet() < 100000000){

                };
                System.out.println(System.currentTimeMillis() - t);
            }
        }.start();
        new Thread(){
            public void run(){
                while (atomicInteger.incrementAndGet() < 100000000){

                };
                System.out.println(System.currentTimeMillis() - t);
            }
        }.start();
        new Thread(){
            public void run(){
                while (atomicInteger.incrementAndGet() < 100000000){

                };
                System.out.println(System.currentTimeMillis() - t);
            }
        }.start();
    }
}
