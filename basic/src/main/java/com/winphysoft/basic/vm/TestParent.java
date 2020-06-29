package com.winphysoft.basic.vm;

/**
 * Created by 小灰灰 on 2017/1/12.
 */
public class TestParent {
    static class A{
        A(){
            init();
        }
        public void init(){
            System.out.println("a");
        }
    }

    static class B extends A{
        private int i = 1;
        B(){

        }
        public void init(){
            System.out.println(i);
        }
    }

    public static void main(String[] args) throws Exception{
        new B();

    }
}
