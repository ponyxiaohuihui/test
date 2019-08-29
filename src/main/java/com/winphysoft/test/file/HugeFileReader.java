package com.winphysoft.test.file;

import java.io.RandomAccessFile;

/**
 * Created by 小灰灰 on 2017/9/21.
 */
public class HugeFileReader {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("D:\\file.dat", "r");
        RandomAccessFile dest = new RandomAccessFile("D:\\1kw.dat", "rw");
        file.getChannel().transferTo(0 , 1000000000, dest.getChannel());
        file.getChannel().close();
        dest.getChannel().close();
        file.close();
        dest.close();
    }
}
