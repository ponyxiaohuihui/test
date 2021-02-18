package com.huihui.basic.io.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author pony
 * Created by pony on 2021/2/18
 */
public class FileFilterDeleter {
    public static void main(String[] args) {
        deleteFile(new File("C:\\data\\songs\\music"), (file, name) -> name.endsWith("wav"));
    }

    private static void deleteFile(File f, FilenameFilter filenameFilter) {
        if (f.isDirectory()){
            for (File file : f.listFiles()) {
                deleteFile(file, filenameFilter);
            }
        } else if (!filenameFilter.accept(f, f.getName())){
            System.out.println("delete file " + f.getName());
            f.delete();
        }
    }
}
