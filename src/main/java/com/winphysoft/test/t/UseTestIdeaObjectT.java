package com.winphysoft.test.t;


import java.util.List;

public class UseTestIdeaObjectT implements TestIdeaObjectT{
    @Override
    public List getValues() {
        return null;
    }

    public static void main(String[] args) {
        List a = new UseTestIdeaObjectT().getValues();
        System.out.println(a);
    }
}
