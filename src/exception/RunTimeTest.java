package exception;

/**
 * Created by pony on 2018/10/10.
 */
public class RunTimeTest {
    public static void main(String[] args) {
        Object ob = create(true);
        System.out.println(ob);
    }

    public static Object create(boolean flag){
        if(flag){
            throw new RuntimeException();
        }
        return new Object();
    }
}
