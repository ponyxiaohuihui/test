package ref;


import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class A extends SuperA{
    public A() {
        super("", 1);
    }

    public static void main(String[] args) throws Exception {
        Class c = A.class;
        Parameter[] parameters = new Parameter[0];
        Class[] types = new Class[0];
        Constructor constructor = c.getDeclaredConstructor(types);
        A a = (A) constructor.newInstance(parameters);
        System.out.println(a);
    }
}
