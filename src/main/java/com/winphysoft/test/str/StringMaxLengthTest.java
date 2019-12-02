package com.winphysoft.test.str;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/9/12
 */
public class StringMaxLengthTest {
    static String s = "";
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 100000; i++) {
            sb.append(i);
        }
        s = sb.toString();
        s.intern();
        System.out.println(s);
    }
}
