package com.huihui.basic.type;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/1/2
 */
public class DoubleCastLongTest {
    public static void main(String[] args) {
        double d = 1.116e99;
        System.out.println((long)d);
        System.out.println(Double.doubleToLongBits(d));
        System.out.println(Double.doubleToRawLongBits(d));
        System.out.println(Double.longBitsToDouble(Double.doubleToLongBits(d)));
    }
}
