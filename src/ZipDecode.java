import com.fr.zip4j.core.ZipFile;

/**
 * Created by 小灰灰 on 2017/3/7.
 */
public class ZipDecode {
    public static void main(String[] args) throws Exception{
        ZipFile file = new ZipFile("C:\\FineReport\\a.zip");
        char[] ps = new char[1];
        while (true){
            try{
                file.setPassword(ps);
                file.extractAll("C:\\FineReport\\ab");
            } catch (Throwable t){
                ps = getPassWord(ps, ps.length - 1);
                continue;
            }
            System.out.println(ps);
            break;
        }
    }

    private static char[] getPassWord(char[] ps, int index){
        if (index >=0 && ps[index] != 255){
            ps[index]+=1;
            return ps;
        }
        return getChars(ps, index);
    }

    private static char[] getChars(char[] ps, int index) {
        if (index == -1){
            return new char[ps.length + 1];
        } else {
            ps[index] = 0;
            return getPassWord(ps, index-1);
        }
    }
}
