package tree;

import com.fr.bi.cal.analyze.cal.multithread.BISingleThreadCal;

import java.util.Iterator;

/**
 * Created by 小灰灰 on 2017/2/23.
 */
public class NodeSBBuilder {

    private int dimensionCount;
    private int row;
    private Node root;

    public NodeSBBuilder(int dimensionCount, int row) {
        this.dimensionCount = dimensionCount;
        this.row = row;
    }

    public Node build() {
        long t = System.currentTimeMillis();
        root = new Node("root", row);
        new SingleChildCal(root, 0).cal();
        System.out.println(System.currentTimeMillis() - t);
        return root;
    }


    private class SingleChildCal implements BISingleThreadCal {
        private Node node;
        private int level;

        public SingleChildCal(Node node, int level) {
            this.node = node;
            this.level = level;
        }

        @Override
        public void cal() {
            cal(node, level);
        }

        private void cal(Node node, int level) {
            if (level >= dimensionCount) {
                return;
            }
            Iterator<Node> it = NodeChildCreator.calChildren(node);
            while (it.hasNext()) {
                Node child = it.next();
                node.addChild(child);
                if (level < dimensionCount) {
                    cal(child, level + 1);
                }
            }
        }
    }
}
