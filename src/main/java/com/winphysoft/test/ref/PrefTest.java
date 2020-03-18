package com.winphysoft.test.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/3/18
 */
public class PrefTest {
    public static void main(String[] args) throws Exception {
       mypref();
//        copyRef();
    }

    private static void copyRef() throws Exception{
        final ReferenceQueue referenceQueue = new ReferenceQueue();
        new Thread() {
            public void run() {
                while (true) {
                    Object obj = referenceQueue.poll();
                    if (obj != null) {
                        try {
                            Field rereferent = Reference.class
                                    .getDeclaredField("referent");
                            rereferent.setAccessible(true);
                            Object result = rereferent.get(obj);
                            System.out.println("gc will collect：" +  result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
        while (true){
            PrefTest abc = new PrefTest();
            System.out.println("new " + abc.hashCode());
            PhantomReference abcWeakRef = new PhantomReference(abc,
                    referenceQueue);
            abc = null;
            Thread.currentThread().sleep(3000);
            System.gc();
            Thread.currentThread().sleep(3000);
        }

    }

    public static void mypref() throws InterruptedException {
        final ReferenceQueue queue = new ReferenceQueue();
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Object ob  = queue.remove();
                        if (ob != null){
                            Field rereferent = Reference.class
                                    .getDeclaredField("referent");
                            rereferent.setAccessible(true);
                            Object result = rereferent.get(ob);
                            System.out.println(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        while (true){
            Thread.sleep(10000);
            PrefTest ob = new PrefTest();
            PhantomReference reference = new PhantomReference(ob, queue);
            System.out.println("new " + ob);
            ob = null;
            System.gc();
        }
    }

}
