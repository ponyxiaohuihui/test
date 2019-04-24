package java.lang;

public class NullClass {
    public void test() {
        Class c = Class.getPrimitiveClass("null");
        System.out.println(c.getName());
    }
}
