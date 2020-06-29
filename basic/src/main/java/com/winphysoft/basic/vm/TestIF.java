package com.winphysoft.basic.vm;

/**
 * Created by 小灰灰 on 2016/10/21.
 */
public class TestIF {
    private Object key;
    private static Object nk = null;
    private int i;
    public static void main(String[] args) {
       new TestIF().run();
    }

    private void init(){
       if (key == nk){
           key = new Object();
       }
    }

    private void run(){
        double[] list = new double[1000000000];
        for (int i = 0; i < list.length; i ++){
            list[i] = Math.random();
        }
        key = nk;
        double sum = 0;
       // key = Math.random() > 0.1 ? null : new Object();
        long t = System.currentTimeMillis();
        for (int i = 0; i < list.length; i++){
            init();
            sum += list[i];
        }
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(i);
        t = System.currentTimeMillis();
        for (int i = 0; i < list.length; i++){
            sum += list[i];
        }
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(sum);
    }
}
