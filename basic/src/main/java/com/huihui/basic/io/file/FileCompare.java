package com.huihui.basic.io.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pony on 2018/9/26.
 */
public class FileCompare {
    public static void main(String[] args) throws IOException {
        BufferedReader a = new BufferedReader(new FileReader("D:\\A"));
        BufferedReader b = new BufferedReader(new FileReader("D:\\B"));
        Set<String> sa = new HashSet<>();
        Set<String> sb = new HashSet<>();
        String s = null;
        while ((s = a.readLine()) != null){
            sa.add(s);
        }
        while ((s = b.readLine()) != null){
            sb.add(s);
        }
        for (String str : sa){
            if (!sb.contains(str)){
                System.out.println(str);
            }
        }
        a.close();
        b.close();
    }
}
