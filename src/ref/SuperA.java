package ref;

public class SuperA {
    String s;
    Integer i;

    public SuperA(String s, Integer i) {
        this.s = s;
        this.i = i;
    }

    @Override
    public String toString() {
        return "SuperA{" +
                "s='" + s + '\'' +
                ", i=" + i +
                '}';
    }
}
