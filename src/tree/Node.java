package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小灰灰 on 2017/2/21.
 */
public class Node {
    private String data;
    private List<Node> children = new ArrayList<Node>();
    private int rowCount;

    public Node(String data, int rowCount) {
        this.data = data;
        this.rowCount = rowCount;
    }

    public String getData() {
        return data;
    }

    public void addChild(Node node){
        children.add(node);
    }

    public int getChildrenLength(){
        return children.size();
    }

    public List<Node> getChildren(){
        return children;
    }

    public int getRowCount() {
        return rowCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        if (rowCount != node.rowCount) {
            return false;
        }
        if (!data.equals(node.data)) {
            return false;
        }
        return children.equals(node.children);

    }

    @Override
    public int hashCode() {
        int result = data.hashCode();
        result = 31 * result + children.hashCode();
        result = 31 * result + rowCount;
        return result;
    }
}
