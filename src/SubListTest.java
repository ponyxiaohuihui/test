import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小灰灰 on 2016/8/5.
 */
public class SubListTest {
    public static void main(String[] args) {
        List l = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            l.add(i);
        }
        System.out.println(l.subList(0, 2));
    }
}
