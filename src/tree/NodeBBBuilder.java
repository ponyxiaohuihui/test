package tree;

import com.fr.bi.cal.analyze.cal.multithread.BIMultiThreadExecutor;
import com.fr.bi.cal.analyze.cal.multithread.BISingleThreadCal;
import com.fr.bi.cal.analyze.cal.multithread.MultiThreadManagerImpl;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 小灰灰 on 2017/2/22.
 */
public class NodeBBBuilder {
    private int dimensionCount;
    private int row;
    //每一层维度计算完成的数量
    private AtomicInteger[] count;
    private Node root;
    //每一层维度被丢进线程池的数量
    private AtomicInteger[] size;

    public NodeBBBuilder(int dimensionCount, int row) {
        this.dimensionCount = dimensionCount;
        this.row = row;
    }

    public Node build() {
        long t = System.currentTimeMillis();
        root = new Node("root", row);
        BIMultiThreadExecutor executor = MultiThreadManagerImpl.getInstance().getExecutorService();
        count = new AtomicInteger[dimensionCount];
        size = new AtomicInteger[dimensionCount];
        for (int i = 0; i < dimensionCount; i++) {
            count[i] = new AtomicInteger(0);
            size[i] = new AtomicInteger(0);
        }
        //为了保证算完，根节点在主线程里面做
        Iterator<Node> it = NodeChildCreator.calChildren(root);
        size[0].incrementAndGet();
        count[0].incrementAndGet();
        while (it.hasNext()) {
            Node child = it.next();
            root.addChild(child);
            //每个节点都丢线程池，计数+1
            size[1].incrementAndGet();
            SingleChildCal cal = new SingleChildCal(child, 1, executor);
            executor.add(cal);
        }
        executor.wakeUp();
        //如果多线程计算没有结束，就等结束
        if (!allCompleted()) {
            synchronized (this) {
                if (!allCompleted()) {
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

    private boolean allCompleted() {
        for (int i = 0; i < count.length; i++) {
            if (count[i].get() != size[i].get() || !currentLevelAllAdded(i)) {
                return false;
            }
        }
        return true;
    }

    private void checkComplete(int level, BIMultiThreadExecutor executor) {
        if (currentLevelAllAdded(level)) {
            //完成了一个维度必须唤醒下线程，要不肯能会wait住死掉。
            executor.wakeUp();
            System.out.println(level);
            synchronized (this) {
                //全部完成了就唤醒下wait的主线程
                if (allCompleted()) {
                    this.notify();
                }
            }
        }
    }

    //当前层的迭代器是否都执行完了
    private boolean currentLevelAllAdded(int level) {
        //最后一层没有迭代器
        if (level == dimensionCount) {
            return false;
        }
        //执行完的迭代器的数量不等于0，并且等于上一层的丢进线程池的计算数量。
        return count[level].get() != 0 && count[level].get() == size[level].get();
    }

    private class SingleChildCal implements BISingleThreadCal {
        private Node node;
        private int level;
        private BIMultiThreadExecutor executor;

        public SingleChildCal(Node node, int level, BIMultiThreadExecutor executor) {
            this.node = node;
            this.level = level;
            this.executor = executor;
        }

        @Override
        public void cal() {
            createExecutors();
            checkComplete(level, executor);
        }

        private void createExecutors() {
            if (level >= dimensionCount) {
                return;
            }
            Iterator<Node> it = NodeChildCreator.calChildren(node);
            while (it.hasNext()) {
                Node child = it.next();
                node.addChild(child);
                if (level < dimensionCount - 1) {
                    //往线程池里加了一个下一层级的计算
                    size[level + 1].incrementAndGet();
                    executor.add(new SingleChildCal(child, level + 1, executor));
                }
            }
            count[level].incrementAndGet();
        }
    }
}
