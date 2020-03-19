package com.winphysoft.test.thread;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/3/19
 */
public class TestSyncVisibleObject {
    public static void main(String[] args) {
        OVChecker checker = new OVChecker();
        new Thread(()->{
            while (true){
                checker.check0();
            }
        }).start();
        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checker.check();
            }
        }).start();
    }

    static class VO{
        int i;

        public VO(int i) {
            this.i = i;
        }
    }

    static class OVChecker{

        private long t = System.currentTimeMillis();
        private  VO vo = new VO(0);
        public synchronized void check(){
            if (vo.i == 0){
                System.out.println(System.currentTimeMillis() - t + " i set 1");
                vo = new VO(1);
            } else {
                System.out.println(System.currentTimeMillis() - t + " i set 0");
                vo = new VO(0);
            }
        }

        public void check0() {
            if (vo.i != 0){
                System.out.println(System.currentTimeMillis() - t + " i is 1");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                checkIS1();
            }
        }

        private void checkIS1() {
            if (vo.i != 0){
                System.out.println(System.currentTimeMillis() - t + " i is check 1");
            }
        }
    }
}
