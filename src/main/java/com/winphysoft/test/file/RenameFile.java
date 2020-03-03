package com.winphysoft.test.file;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RenameFile {
    public static void main(String[] args) {
        String path = "C:\\Finereport\\data\\hdm\\";
        int start = 1667;
        List<File> files = Arrays.asList(new File(path).listFiles());
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return getOrder(o1.getName()).compareTo(getOrder(o2.getName()));
            }
        });
        for (File file : files) {
            File renameFile = new File(path + "REC" + start + ".mp3");
            System.out.println("rename " + file.getName() + " to " + renameFile.getName());
            file.renameTo(renameFile);
            start++;
        }
    }

//    private static Double getOrder(String s){
//        s = s.substring(1, s.lastIndexOf('.'));
//        s = s.replace('p', ' ');
//        s = s.replace('-', '.');
//        return Double.parseDouble(s);
//    };

    private static Integer getOrder(String s){
        int idx = s.indexOf('-');
        int number = Integer.parseInt(s.substring(2, idx));
        int high = s.charAt(idx + 1) * 100;
        return high + number;
    };
}
