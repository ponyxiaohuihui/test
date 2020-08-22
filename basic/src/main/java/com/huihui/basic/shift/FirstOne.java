package com.huihui.basic.shift;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/11/12
 */
public class FirstOne {
    public static void main(String[] args) {
        shiftPositiveInt(10000000);
    }

    private static void shiftPositiveInt(int i){
        assert i>=0;
        int idx = 0;
        while (i << ++idx > 0){

        }
        System.out.println(idx);
    }
}
