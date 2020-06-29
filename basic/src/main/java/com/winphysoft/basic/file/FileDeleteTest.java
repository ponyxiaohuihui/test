package com.winphysoft.basic.file;

import java.io.File;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/3/27
 */
public class FileDeleteTest {
    public static void main(String[] args) {
        File f = new File("D:\\develop\\data\\db\\GLOBAL_STRING_DIC");
        deleteFile(f);
    }

    private static void deleteFile(File f) {
        if (!f.isFile()) {
            for (File file : f.listFiles()) {
                deleteFile(file);
            }
        }
        f.delete();
    }
}
