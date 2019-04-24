package com.winphysoft.test.method;

public class SubIVisitor {
    public static void visit(I i){
        System.out.println("i");
    }

    public static void visit(SubI i){
        System.out.println("subi");
    }

    public static void main(String[] args) {
        SubIVisitor.create();
    }

    public static void create(){
        visit(getI());
    }

    public static I getI(){
        return new SubIImpl();
    }
}
