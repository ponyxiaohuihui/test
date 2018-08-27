import sun.misc.VM;

import java.nio.ByteBuffer;

/**
 * Created by 小灰灰 on 2017/1/18.
 */
public class Buffer {
    public static void main(String[] args) {
        VM.maxDirectMemory();
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024*1024*100);
    }
}
