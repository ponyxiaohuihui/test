/**
 * Created by pony on 2018/8/16.
 */
public class FinalTest {
    public final int i;

    public FinalTest(int i) {
        this.i = i;
    }

    public static void main(String[] args) {
        System.out.println(new FinalTest(10));
    }
}
