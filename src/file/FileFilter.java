package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by pony on 2017/10/24.
 */
public class FileFilter {
    private static final String FILTER = "ERROR [com.finebi.cube.common.log.BILogger] ";
    private static final String EXCEPTION =  "java.lang.NullPointerException";
    public static void main(String[] args) throws Exception{
        File f = new File("C:\\FineReport\\a.log");
        BufferedReader reader = new BufferedReader(new FileReader(f));
        File out = new File("C:\\FineReport\\b.log");
        BufferedWriter writer = new BufferedWriter(new FileWriter(out));
        String s;
        String[] result = new String[2];
        int index = 0;
        while ((s = reader.readLine()) != null){
            if (index == 2){
                if (result[0].equals(EXCEPTION) && result[1].endsWith(FILTER)){

                } else if  (result[1].equals(EXCEPTION) && result[0].endsWith(FILTER)){

                }else {
                    writer.write(result[0]);
                    writer.newLine();
                    writer.write(result[1]);
                    writer.newLine();
                }
                index =0;
            }
            result[index] = s;
            index++;
        }
        writer.flush();
        reader.close();
        writer.close();

    }
}
