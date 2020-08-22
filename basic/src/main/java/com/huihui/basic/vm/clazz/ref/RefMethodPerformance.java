package com.huihui.basic.vm.clazz.ref;

public class RefMethodPerformance {
    private RefMethodPerformance performance;
    int getValue(){
        return performance == null ? 1 : performance.getValue();
    }

    void setPerformance(RefMethodPerformance performance){
        this.performance = performance;
    }

    static void testSingle(){
        int count = 100000000;
        RefMethodPerformance performance = new RefMethodPerformance();
        int sum = 0;
        long t = System.currentTimeMillis();
        while (count-- > 0){
            sum+= performance.getValue();
        }
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(sum);
    }


    static void test10(){
        int count = 100000000;
        RefMethodPerformance performance = new RefMethodPerformance();
        int size = 0;
        RefMethodPerformance performance1 = performance;
        RefMethodPerformance performance2 = new RefMethodPerformance();
        while (size-- > 0){
            performance1.setPerformance(performance2);
            performance1 = performance2;
            performance2 = new RefMethodPerformance();
        }
        int sum = 0;
        long t = System.currentTimeMillis();
        while (count-- > 0){
            sum+= performance.getValue();
        }
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(sum);
    }

    public static void main(String[] args) {
        while (true){
            test10();
        }

    }
}
