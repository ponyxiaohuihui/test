package com.winphysoft.basic.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/10/10
 */
public class TestWaitFgc {
    static Resource resource = new Resource();

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        //一个线程每100毫秒模拟下fullgc
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                        for (int i = 0; i < 1000000; i++) {
                            list.add(i);
                        }
                    } catch (Throwable e) {
                    }
                }
            }
        }.start();
        //4个线程同时读

        for (int i = 0; i < 2000; i++) {
            int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        Resource rsc = resource;
                        //判断状态，检查是否需要初始化,如果不可读，就去初始化
                        if (!rsc.isReadable()) {
                            initResource();
                        } else {
                            //获取对象，如果为空print一下
//                            try {
//                                Thread.sleep(50);
//                            } catch (InterruptedException e) {
//                            }
                            Object ob = rsc.getOb();
                            if (ob == null){
                                System.out.println("i am " + finalI + " ob is null " + (ob == null));
                            }
                        }
                    }
                }
            }.start();
        }
        //主线程释放资源
        while (true) {
            //先标记不可读
            Resource rsc = resource;
            rsc.close();
            long t;
            Thread.yield();
            do {
                t= System.currentTimeMillis();
                synchronized (rsc) {
                    try {
                        rsc.wait(100);
                    } catch (InterruptedException e) {

                    }
                }
            } while (notWaitEnough(t));
            Thread.yield();
            rsc.clear();
        }
    }

    public static boolean notWaitEnough(long t) {
        long t1 =  System.currentTimeMillis() -t;
        if (t1 > 120){
            System.out.println(t1);
            return true;
        }
        return false;
    }

    synchronized static void initResource() {
        resource = new Resource();
    }

}

class Resource {
    private volatile Object ob = new Object();
    private volatile boolean readable = true;

    public Resource() {
    }

    public boolean isReadable() {
        return readable;
    }

    public Object getOb() {
        return ob;
    }

    public void close() {
        readable = false;
        //System.out.println("close");
    }

    public void clear() {
        ob = null;
        //System.out.println("clear");
    }

}
