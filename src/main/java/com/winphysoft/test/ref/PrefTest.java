package com.winphysoft.test.ref;

import sun.misc.Cleaner;
import sun.misc.SharedSecrets;

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
    private Cleaner cleaner;
    private int id;


    public PrefTest(int id) {
        this.id = id;
        this.cleaner = Cleaner.create(this, new PrefCleaner(id));
    }

    public static void main(String[] args) throws Exception {
        cleanerPref();
//        copyRef();
    }

    private static void copyRef() throws Exception {
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
                            System.out.println("gc will collect：" + result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
        int i = 0;
        while (true) {
            PrefTest abc = new PrefTest(i++);
            System.out.println("new " + abc.hashCode());
            PhantomReference abcWeakRef = new PhantomReference(abc,
                    referenceQueue);
            abc = null;
            Thread.currentThread().sleep(3000);
            System.gc();
            Thread.currentThread().sleep(3000);
        }

    }

    public static void myPref() throws InterruptedException {
        final ReferenceQueue queue = new ReferenceQueue();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object ob = queue.remove();
                        if (ob != null) {
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
        int i = 0;
        while (true) {
            Thread.sleep(10000);
            PrefTest ob = new PrefTest(i++);
            PhantomReference reference = new PhantomReference(ob, queue);
            System.out.println("new " + ob);
            ob = null;
            System.gc();
        }
    }

    public static void cleanerPref() throws InterruptedException {
        int i = 0;
        while (true) {
            Thread.sleep(1000);
            PrefTest ob = new PrefTest(i++);
            System.out.println("new " + ob);
//            ob = null;
            System.gc();
        }
    }

    @Override
    public String toString() {
        return "PrefTest{" +
                "id=" + id +
                '}';
    }

    protected static class PrefCleaner implements Runnable {

        private int id;

        public PrefCleaner(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("release " + id);
        }
    }

}
