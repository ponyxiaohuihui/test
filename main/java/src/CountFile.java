import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pony on 2018/4/2.
 */
public class CountFile {

    public static void main(String[] args) {
        String path = "C:\\FineReport\\develop\\finebi\\5.0\\nuclear-swift";

        ArrayList<File> al = getFile(new File(path));
        for (File f : al) {
            if (f.getName().endsWith("java")) {
                count(f);
                System.out.println(f);
            }
        }
        System.out.println("统计文件：" + files);
        System.out.println("代码行数：" + codeLines);
    }

    static long files = 0;
    static long codeLines = 0;
    static ArrayList<File> fileArray = new ArrayList<File>();

    /**
     * 获得目录下的文件和子目录下的文件
     *
     * @param f
     * @return
     */
    public static ArrayList<File> getFile(File f) {
        File[] ff = f.listFiles();
        for (File child : ff) {
            if (child.isDirectory()) {
                getFile(child);
            } else
                fileArray.add(child);
        }
        return fileArray;

    }

    /**
     * 统计方法
     *
     * @param f
     */
    private static void count(File f) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            while ( br.readLine() != null) {
                    codeLines++;
            }
            files++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
