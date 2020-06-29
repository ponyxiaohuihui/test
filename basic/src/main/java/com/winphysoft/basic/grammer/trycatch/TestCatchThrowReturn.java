package com.winphysoft.basic.grammer.trycatch;

public class TestCatchThrowReturn {
    public static void main(String[] args) {
        System.out.println(getString(1));
    }

    private static String getString(int i){
        try {
            if (i == 1){
                throw new RuntimeException();
            }
        } catch (Exception e){
            throwException();
        }
        return "notcatch";
    }

    private static void throwException() {
        throw new RuntimeException();
    }
}
