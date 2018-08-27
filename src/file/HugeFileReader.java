package file;

import java.io.RandomAccessFile;

/**
 * Created by 小灰灰 on 2017/9/21.
 */
public class HugeFileReader {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("C:\\FineReport\\develop\\a.log", "r");
        RandomAccessFile dest = new RandomAccessFile("C:\\FineReport\\develop\\b.log", "rw");
        file.getChannel().transferTo(file.length() - 10000000, file.length(), dest.getChannel());
    }
}
