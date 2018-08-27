/**
 * Created by 小灰灰 on 2016/12/22.
 */
public class A {
    public static void main(String[] args) {
        Object a = new A();
        p(a.getClass().cast(a));
    }

    public static void p(Object ob){
        System.out.println("ob");
    }

    public static void p(A ob){
        System.out.println("a");
    }
}
