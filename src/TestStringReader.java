import com.finebi.cube.data.disk.reader.BIByteArrayNIOReader;
import com.finebi.cube.data.disk.reader.BIStringNIOReader;
import com.finebi.cube.data.disk.reader.primitive.BIByteNIOReader;
import com.finebi.cube.data.disk.reader.primitive.BIIntegerNIOReader;
import com.finebi.cube.data.disk.reader.primitive.BILongNIOReader;

/**
 * Created by pony on 2018/1/22.
 */
public class TestStringReader {
    public static void main(String[] args) throws Exception{
        BILongNIOReader position = new BILongNIOReader("C:\\FineReport\\detail.fbi.fp");
        BIIntegerNIOReader length = new BIIntegerNIOReader("C:\\FineReport\\detail.fbi.fl");

        BIByteNIOReader content = new BIByteNIOReader("C:\\FineReport\\detail.fbi");

        BIByteArrayNIOReader b = new BIByteArrayNIOReader(position, length, content);
        BIStringNIOReader reader = new BIStringNIOReader(b);
        for (int i = 0; i < 100; i++){
            System.out.println(reader.getSpecificValue(i));
        }
    }
}
