package cls;

public class MethodOverloadTest {
    public void out(String s, Throwable ob){
        System.out.println("th");
    }

    public void out(String s, Exception ex){
        System.out.println("ex");
    }

    public static void main(String[] args) {
        new MethodOverloadTest().out("a", new RuntimeException());
    }
}
