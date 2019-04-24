package file;

import java.io.RandomAccessFile;

/**
 * Created by 小灰灰 on 2017/9/21.
 */
public class HugeFileReader {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("D:\\monitor.design_error\\monitor.design_error.csv", "r");
        RandomAccessFile dest = new RandomAccessFile("D:\\monitor.design_error\\s.csv", "rw");
        file.getChannel().transferTo(0 , 1000000, dest.getChannel());
        file.getChannel().close();
        dest.getChannel().close();
        file.close();
        dest.close();
    }
}
