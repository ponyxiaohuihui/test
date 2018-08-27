/**
 * Created by 小灰灰 on 2017/6/13.
 */
public class CastTest {

    public static void main(String[] args) {
        Parent p = new Parent();
        System.out.println(((Child)p).a);
    }

    static class Parent{
        String n = "p";
    }

    static class Child extends Parent{
        String a = "c";
    }
}

