package com.huihui.basic.type;

/**
 * Created by 小灰灰 on 2017/2/9.
 */
public class TestArrayClone {
    public static void main(String[] args) {
        Object[] obs = new Object[1];
        for (int i = 0; i < obs.length; i++){
            obs[i] = new Object();
        }
        System.out.println(obs[0] == obs.clone()[0]);
    }
}
