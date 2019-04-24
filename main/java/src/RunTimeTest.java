import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * Created by pony on 2018/6/28.
 */
public class RunTimeTest {
    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        int[] arrays = new int[100000000];
        System.out.println(rt.freeMemory()/1000000000);
        System.out.println(rt.maxMemory()/1000000000);
        System.out.println(rt.totalMemory()/1000000000);
        System.out.println(rt.totalMemory() - rt.freeMemory());
        System.out.println(memoryMXBean.getHeapMemoryUsage().getUsed()/1000000000);
        System.out.println(memoryMXBean.getHeapMemoryUsage().getMax()/1000000000);
        System.out.println(memoryMXBean.getHeapMemoryUsage().getInit()/1000000000);

    }
}
