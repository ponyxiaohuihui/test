package file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class RandomAccessFileTest {
    public static void main(String[] args) throws IOException {
        FileChannel fc = new RandomAccessFile("D://testdata1",  "rw").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new String("bb").getBytes());
        fc.write(buffer);
        fc.close();
    }
}
