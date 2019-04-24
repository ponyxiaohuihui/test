import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListIteratorTest {
    public static void main(String[] args) {
        List list = new LinkedList();
        list.add("1");
        Iterator it = list.iterator();
        list.add("2");
        while (it.hasNext()){
            System.out.println(it.next());
        }
    }
}
