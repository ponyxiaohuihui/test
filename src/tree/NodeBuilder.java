package tree;

import com.fr.bi.cal.analyze.cal.multithread.BIMultiThreadExecutor;
import com.fr.bi.cal.analyze.cal.multithread.BISingleThreadCal;
import com.fr.bi.cal.analyze.cal.multithread.MultiThreadManagerImpl;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 小灰灰 on 2017/2/21.
 */
public class NodeBuilder {
    private int dimensionCount;
    private int row;
    private AtomicInteger count;
    private volatile boolean allAdded;
    private volatile boolean completed;
    private Node root;

    public NodeBuilder(int dimensionCount, int row) {
        this.dimensionCount = dimensionCount;
        this.row = row;
    }

    public Node build(){
        long t = System.currentTimeMillis();
        root = new Node("root", row);
        BIMultiThreadExecutor executor  = MultiThreadManagerImpl.getInstance().getExecutorService();
        count = new AtomicInteger(0);
        allAdded = false;
        Iterator<Node> it = NodeChildCreator.calChildren(root);
        completed = !it.hasNext();
        while (it.hasNext()){
            Node child = it.next();
            root.addChild(child);
            SingleChildCal cal = new SingleChildCal(child, 1);
            executor.add(cal);
        }
        allAdded = true;
        completed = completed || count.get() == root.getChildrenLength();
        //如果多线程计算没有结束，就等结束
        if (!completed){
            executor.wakeUp();
            synchronized (this){
                if (!completed){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        System.out.println(System.currentTimeMillis() - t);
        return root;
    }

    private void checkComplete(){
        //如果已经计算完了加入线程池的计算，并且所有计算都已经加入线程池了，就说明计算完了，唤醒下等待的线程。
        if (count.incrementAndGet() == root.getChildrenLength() && allAdded){
            synchronized (this){
                completed = true;
                this.notify();
            }
        }
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
            checkComplete();
        }

        private void cal(Node node, int level) {
            if (level >= dimensionCount){
                return;
            }
            Iterator<Node> it = NodeChildCreator.calChildren(node);
            while (it.hasNext()){
                Node child = it.next();
                node.addChild(child);
                if (level < dimensionCount){
                    cal(child, level + 1);
                }
            }
        }
    }
}
