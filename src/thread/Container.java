package thread;

import java.util.List;

/**
 * Created by 小灰灰 on 2017/8/18.
 */
public class Container {
    private volatile boolean isEnd;
    private List list;
    private int index;

    public void addData(Object data){
        list.add(data);
    }

    //返回是否结束
    public boolean consume(){
        consume(list.get(index++));
        if (isEnd && list.size() <= index){
            end();
            return true;
        }
        return false;
    }

    public void consume(Object data){
        //这个data是string明细的数组，这边生成索引
    }

    public void end(){
        //生成完了可以做点事情
    }


}
