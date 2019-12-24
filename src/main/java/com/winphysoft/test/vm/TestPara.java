package com.winphysoft.test.vm;

/**
 * Created by 小灰灰 on 2017/2/15.
 */
public class TestPara {
    public static void main(String[] args) {
        new Para().print();
    }

    static class Para{
        private int a = 0;
        public void print(){
            printa(a , setA());
            a = 0;
            printb(setA(), a);
        }

        public void printa(int a, int b){
            System.out.println(a);
        }
        public void printb(int a, int b){
            System.out.println(b);
        }

        private int setA() {
            a = 1;
            return 2;
        }
    }
}
