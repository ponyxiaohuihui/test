package tree;

import java.util.Iterator;

/**
 * Created by 小灰灰 on 2017/2/22.
 */
public class NodeChildCreator {

    public static Iterator<Node> calChildren(Node node){
     //   int count = node.getData().equals("root")? 1 : 10;
        return calChildren(node.getRowCount(), 10);
    }

    private static Iterator<Node> calChildren(final int rowCount, final int count){
        try {
            Thread.sleep(rowCount / 10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Iterator<Node>() {
            private int size = count;

            private int totalCount = rowCount;
            @Override
            public boolean hasNext() {
                return size > 0;
            }
            @Override
            public Node next() {
                size--;
                return new Node(size+"", getCount());
            }

            public int getCount() {
                if (size == 0){
                    return totalCount;
                }
                int count =  totalCount  / (size + 1);
                count = Math.max(1, count);
                totalCount -= count;
                return count;
            }

            @Override
            public void remove() {

            }
        };
    }

}
