package tree;

import com.fr.bi.cal.analyze.cal.multithread.BIMultiThreadExecutor;
import com.fr.bi.cal.analyze.cal.multithread.BISingleThreadCal;
import com.fr.bi.cal.analyze.cal.multithread.MultiThreadManagerImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 小灰灰 on 2017/2/22.
 */
public class NodeBBuilder {
    private int dimensionCount;
    private int row;
    private AtomicInteger count;
    private volatile boolean allAdded;
    private volatile boolean completed;
    private int size;
    private Node root;

    public NodeBBuilder(int dimensionCount, int row) {
        this.dimensionCount = dimensionCount;
        this.row = row;
    }

    public Node build() {
        long t = System.currentTimeMillis();
        root = new Node("root", row);
        BIMultiThreadExecutor executor = MultiThreadManagerImpl.getInstance().getExecutorService();
        count = new AtomicInteger(0);
        allAdded = false;
        List<SingleChildCal> calList = new ArrayList<SingleChildCal>();
        int index = 0;
        while (true){
            //到第n层维度满足就退出
            if (checkThread(root, index++, calList, executor, false)){
                break;
            } else {
                //如果不满足,就把上一层的清掉，
                calList.clear();
            }
        }
        allAdded = true;
        completed = completed || count.get() == size;
        //如果多线程计算没有结束，就等结束
        if (!completed) {
            executor.wakeUp();
            synchronized (this) {
                if (!completed) {
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


    private boolean checkThread(Node node, int level, List<SingleChildCal> calList, BIMultiThreadExecutor executor, boolean isEnough) {
        if (level >= dimensionCount) {
            return true;
        }
        return checkThread(node, level, level, calList, executor, isEnough);
    }

    private boolean checkThread(Node node, int currentLevel, int nodeLevel, List<SingleChildCal> calList, BIMultiThreadExecutor executor, boolean isEnough) {
        //到level==0的时候表明是该层级的node，不是的就取子节点
        if (nodeLevel == 0){
            Iterator<Node> it = NodeChildCreator.calChildren(node);
            while (it.hasNext()) {
                Node child = it.next();
                node.addChild(child);
                //如果满足个数就加到线程
                if (isEnough) {
                    executor.add(new SingleChildCal(child, currentLevel + 1));
                    size++;
                } else {
                    if (isEnough) {
                        executor.add(new SingleChildCal(child, currentLevel + 1));
                        size++;
                    } else {
                        calList.add(new SingleChildCal(child, currentLevel + 1));
                    }
                    isEnough = checkEnough(calList, executor);
                }
            }
            return isEnough;
        } else {
            for (Node n : node.getChildren()){
                isEnough =  checkThread(n, currentLevel, nodeLevel - 1, calList, executor, isEnough) || isEnough;
            }
            return isEnough;
        }

    }

    //如果子节点数量大于16就说明线程数够了，可以从此加入多线程
    private boolean checkEnough(List<SingleChildCal> calList, BIMultiThreadExecutor executor) {
        //如果满足了就全部加到线程池
        if (calList.size() > 15) {
            for (BISingleThreadCal cal : calList) {
                executor.add(cal);
                size++;
            }
            calList.clear();
            return true;
        }
        return false;
    }

    private void checkComplete() {
        //如果已经计算完了加入线程池的计算，并且所有计算都已经加入线程池了，就说明计算完了，唤醒下等待的线程。
        if (count.incrementAndGet() == size && allAdded) {
            synchronized (this) {
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
