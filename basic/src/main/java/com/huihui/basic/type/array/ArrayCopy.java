package com.huihui.basic.type.array;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/1/9
 */
public class ArrayCopy {
    public static void main(String[] args) {
        int[] ints = {0, 1, 2, 3, 4};
        System.arraycopy(ints, 1, ints, 0, 3);
        System.out.println(ints);
    }
}
