package com.winphysoft.test.file;

import java.io.RandomAccessFile;

/**
 * Created by 小灰灰 on 2017/9/21.
 */
public class HugeFileReader {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("C:\\codes\\f.log", "r");
        RandomAccessFile dest = new RandomAccessFile("C:\\codes\\f3.log", "rw");
        file.getChannel().transferTo(2000000000 , 10000000, dest.getChannel());
        file.getChannel().close();
        dest.getChannel().close();
        file.close();
        dest.close();
    }
}
