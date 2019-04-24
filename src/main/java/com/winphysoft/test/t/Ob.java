package t;

/**
 * Created by pony on 2018/8/29.
 */
public class Ob extends T {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Ob ob = new Ob();
        Object t = new Object();
        ob.set(t);
    }
}
