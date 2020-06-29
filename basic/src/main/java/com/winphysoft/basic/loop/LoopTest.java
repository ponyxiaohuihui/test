package com.winphysoft.basic.loop;

public class LoopTest {
    private int size(){
        System.out.println("size");
        return 300000000;
    }

    public static void main(String[] args) {
        LoopTest test = new LoopTest();
        for (int i = 0; i < test.size(); i++){
            System.out.println("t");
        }
    }
}
