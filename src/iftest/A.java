package iftest;

/**
 * Created by 小灰灰 on 2016/10/13.
 */
public class A implements Interface {
    @Override
    public void test(Interface i) {
        System.out.println("555");
    }

    @Override
    public String getOb() {
        return null;
    }

    private void testa(A a){
        System.out.println("aa");
    }

    public void test(B b){
        System.out.println("ab");
    }

    public static B getB(){
        return new B();
    }
}
