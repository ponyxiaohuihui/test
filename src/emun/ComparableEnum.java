package emun;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pony on 2017/11/8.
 */
public enum ComparableEnum {
    B,A,C;
    public static void main(String[] args){
        TreeMap<ComparableEnum, Object> map = new TreeMap<>();
        map.put(ComparableEnum.B, "B");
        map.put(ComparableEnum.A, "A");
        map.put(ComparableEnum.C, "C");
        for (Map.Entry entry : map.entrySet()){
            System.out.println(entry.getKey());
        }
    }
}
