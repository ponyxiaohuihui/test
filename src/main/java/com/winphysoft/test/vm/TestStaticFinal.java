package com.winphysoft.test.vm;

/**
 * Created by pony on 2017/10/23.
 */
public class TestStaticFinal {

    public static void main(String[] args) {
        System.out.println(TestStatic.a);
        System.out.println(TestSF.a);
    }
}

class TestStatic{
    public static int a= 1;
    static {
        System.out.println("shahaha");
    }

}

class TestSF{
    public static final int a= 1;
    static {
        System.out.println("sfhahaha");
    }

}
