import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by 小灰灰 on 2017/6/2.
 */
public class LinuxTestConnection {
    public static void main(String[] args) {
        while (true){
            try {
                long t = System.currentTimeMillis();
                Process process = Runtime.getRuntime().exec("curl 172.170.3.145/WebReport?ReportServer?op=fs");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s = reader.readLine();
                if (s != null){
                    System.out.println(s);
                }
                long time = System.currentTimeMillis() - t;
                if (time > 2000){
                    System.out.println(time);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
