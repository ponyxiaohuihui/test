/**
 * Created by 小灰灰 on 2017/8/11.
 */
public class TTest<T> {
    public void print(T t){
        System.out.println(t);
    }
    public static void main(String[] args) {
        TTest t = new TTest<Long>();
        Object ob = new Integer(1);
        t.print(ob);
    }
}
