package com.winphysoft.basic.io.file;

import java.io.RandomAccessFile;

/**
 * Created by 小灰灰 on 2017/9/21.
 */
public class HugeFileReader {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("C:\\Users\\zihai\\Documents\\WXWork\\1688853806280615\\Cache\\File\\2019-12\\FineIndex.log", "r");
        RandomAccessFile dest = new RandomAccessFile("C:\\Users\\zihai\\Documents\\WXWork\\1688853806280615\\Cache\\File\\2019-12\\FineIndex1.log", "rw");
        file.getChannel().transferTo(2000000000l , 1000000000, dest.getChannel());
        file.getChannel().close();
        dest.getChannel().close();
        file.close();
        dest.close();
    }
}
