/**
 * Created by 小灰灰 on 2016/12/28.
 */
public class NullTest {
    public static void main(String[] args) {
        Integer i = null;
        Object ob = i == null ? i.intValue() : i.longValue();
        System.out.println(ob);
    }
}
