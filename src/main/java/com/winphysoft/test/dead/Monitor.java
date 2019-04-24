package com.winphysoft.test.dead;

public class Monitor {

    private String mind;
    private Monitor target;

    public Monitor(String mind) {
        this.mind = mind;
    }

    public void setTarget(Monitor target) {
        this.target = target;
    }

    public static void main(String[] args) {
        Monitor a  = new Monitor("pony monitor yee");
        Monitor b = new Monitor("yee monitor pony");
        a.setTarget(b);
        b.setTarget(a);
        System.out.println(a.getMind());
    }

    public String getMind() {
        return monitor(target);
    }

    private String monitor(Monitor b) {
        return b.getMind();
    }
}
