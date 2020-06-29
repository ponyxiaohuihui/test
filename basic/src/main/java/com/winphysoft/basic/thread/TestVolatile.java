package com.winphysoft.basic.thread;

public class TestVolatile {
    private  volatile boolean f = false;
    public static void main(String... args) throws InterruptedException {
        final int[] ds = new int[100000000];
        for (int i = 0; i < ds.length;i++){
            ds[i]= (int)Math.random()*i;
        }
        final TestVolatile t = new TestVolatile();
//        new Thread(){
//            public void run(){
//                t.f = !t.f;
//            }
//        }.start();
        Thread t1 = new Thread(){
            public void run(){
                int index = 0;
                long time = System.currentTimeMillis();
                for (int i = 0; i < ds.length; i++){
                    if (!t.isF()){
                        index+=ds[i];
                    }
                }
                System.out.println(System.currentTimeMillis() - time);
                t.f = true;
                time = System.currentTimeMillis();
                for (int i = 0; i < ds.length; i++){
                    if (t.isF()){
                        index+=ds[i];
                    }
                }
                System.out.println(System.currentTimeMillis() - time);
                time = System.currentTimeMillis();
                for (int i = 0; i < ds.length; i++){
                    index+=ds[i];
                }
                System.out.println(System.currentTimeMillis() - time);
                System.out.println(index);
            }
        };
        t1.start();
        t1.join();
//        int index = 0;
//        long time = System.currentTimeMillis();
//        for (int i = 0; i < ds.length; i++){
//            if (!t.isF()){
//                 index+=ds[i];
//            }
//        }
//        System.out.println(System.currentTimeMillis() - time);
//        t.f = true;
//        time = System.currentTimeMillis();
//        for (int i = 0; i < ds.length; i++){
//            if (t.isF()){
//                index+=ds[i];
//            }
//        }
//        System.out.println(System.currentTimeMillis() - time);
//        time = System.currentTimeMillis();
//        for (int i = 0; i < ds.length; i++){
//            index+=ds[i];
//        }
//        System.out.println(System.currentTimeMillis() - time);
//        System.out.println(index);

    }

    public boolean isF() {
        return f;
    }
}
