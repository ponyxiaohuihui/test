import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pony on 2017/12/13.
 */
public class TestMap {
    public static void main(String[] args) {
        Map m = new ConcurrentHashMap();
        m.put(1, 1);
        System.out.println(m.get(2));
        System.out.println(new String("   ").trim());
    }
}
