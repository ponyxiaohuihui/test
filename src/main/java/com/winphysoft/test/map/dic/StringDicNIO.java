package com.winphysoft.test.map.dic;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/3/5
 */
public class StringDicNIO {
    private static StringMapNIO string2Int;

    public static int alias(String v) {
        if (v == null) {
            v = "fb___null___";
        }
        return string2Int.get(v);
    }

//    public static String strValue(int v) {
//        if (Null.isNull(v)) {
//            return null;
//        }
//        String r = (String) int2Dic.get(v);
//        if (r.equals("fb___null___")) {
//            return null;
//        } else {
//            return r;
//        }
//    }
//
//    public static Map<String, Integer> getDic2Int() {
//        return dic2Int;
//    }
//
//    /**
//     * 将 dic2Int 保存到磁盘
//     */
//    public static void saveToDisk(String path) {
//        synchronized (dic2Int) {
//            LZ4Writer writer = new LZ4Writer(new File(path));
//            writer.writeInt(dic2Int.size());
//            dic2Int.forEach((String k, Integer v) -> {
//                writer.writeInt(v);
//                writer.writeString(k);
//            });
//            writer.close();
//        }
//    }
//
//    public static void readFromDisk() {
//        readFromDisk(Paths.get(BenchmarkConf.getPath(), "db", "GLOBAL_STRING_DIC").toString());
//    }
//    public static void readFromDisk(String path) {
//        File f = new File(path);
//        if (f.exists()){
//            synchronized (dic2Int) {
//                LZ4Reader reader = new LZ4Reader(f);
//                int stringCnt = reader.readInt();
//                for (int i = 0; i < stringCnt; i++) {
//                    int alias = reader.readInt();
//                    String v = reader.readString();
//                    dic2Int.put(v, alias);
//                    int2Dic.put(alias, v);
//                }
//                count.set(stringCnt);
//            }
//        }
//    }
//
//    public static boolean isNull(String v) {
//        return "fb___null___".equals(v);
//    }
//
//    public static void saveToDisk() {
//        saveToDisk(Paths.get(BenchmarkConf.getPath(), "db", "GLOBAL_STRING_DIC").toString());
//    }
}
