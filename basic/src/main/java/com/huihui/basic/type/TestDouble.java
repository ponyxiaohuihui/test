package com.huihui.basic.type;

/**
 * Created by 小灰灰 on 2017/4/25.
 */
public class TestDouble {
    public static void main(String[] args) {
        double[] ds = new double[200000000];
        for (int i = 0; i < ds.length;i++){
            ds[i] = Math.random();
        }
        long t = System.currentTimeMillis();
        double s =  0;
        for (int i = 0; i < ds.length;i++){
            s+=ds[i];
        }
        System.out.println(s);
        System.out.println(System.currentTimeMillis() - t);
    }
}
