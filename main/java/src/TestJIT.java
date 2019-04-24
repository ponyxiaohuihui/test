import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by 小灰灰 on 2017/9/12.
 */
public class TestJIT {
    public static void main(String[] args) {
        TestJIT test = new TestJIT();
        for (int i = 0; i < 1000; i++){
            test.testSort(10);
        }
    }

    private void testSort(int iterations){
        long sum = 0;
        Object lock = new Object();
        for (int i = 0;i < iterations; i++){
            synchronized (lock){
                Random random = new Random();
                int count = 1000;
                List<Integer> list = new ArrayList<>();
                for (int j = 0; j < count; j++){
                    list.add(random.nextInt());
                }
                Collections.sort(list);
                for (int j = 0; j < count; j++){
                    sum += list.get(j);
                }
            }
        }
        System.out.println(sum);
    }
}
