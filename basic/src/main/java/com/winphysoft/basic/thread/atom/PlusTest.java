package com.winphysoft.basic.thread.atom;

public class PlusTest {
    int num = 0;
    public static void main(String[] args) {
        PlusTest test = new PlusTest();
        new Thread(){
            public void run(){
                for (int i = 0; i < 1000000;i++){
                    test.num++;
                }
                System.out.println(test.num);
            }
        }.start();
        new Thread(){
            public void run(){
                for (int i = 0; i < 1000000;i++){
                    test.num++;
                }
                System.out.println(test.num);
            }
        }.start();
    }
}
