package file;

import java.io.RandomAccessFile;

/**
 * Created by 小灰灰 on 2017/9/21.
 */
public class HugeFileReader {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("C:\\FineReport\\output.log", "r");
        RandomAccessFile dest = new RandomAccessFile("C:\\FineReport\\1.log", "rw");
        file.getChannel().transferTo(file.length() - 360000000, file.length()- 320000000, dest.getChannel());
    }
}
