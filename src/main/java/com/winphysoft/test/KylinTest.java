import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Created by pony on 2018/5/3.
 */
public class KylinTest {
    public static void main(String[] args) throws Exception{
        Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();
        System.setProperty("user.timezone","GMT +01");
        Properties info = new Properties();
        info.put("user", "ADMIN");
        info.put("password", "KYLIN");
        Connection conn = driver.connect("jdbc:kylin://env.finedevelop.com:57307/learn_kylin", info);
        Statement state = conn.createStatement();
        ResultSet resultSet = state.executeQuery("select KYLIN_CAL_DT.CAL_DT from KYLIN_CAL_DT ");

        while (resultSet.next()) {
            Date date = resultSet.getDate(1, Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))) ;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            System.out.println(sdf.format(date));
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            System.out.println(sdf.format(date));
            break;

        }

        resultSet.close();
        state.close();
        conn.close();

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        TimeZone.getTimeZone("GMT+8").getOffset(System.currentTimeMillis());
        c.clear();
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.MONTH, 10);
        c.set(Calendar.DAY_OF_MONTH, 11);
        System.out.println(c.getTimeInMillis());
        c = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        c.clear();
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.MONTH, 10);
        c.set(Calendar.DAY_OF_MONTH, 11);
        System.out.println(c.getTimeInMillis());

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
//        java.util.Date date = sdf.parse("2012-01-01 00:00:00");
//        System.out.println(date.getTime());
//        SimpleDateFormat sdf8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        java.util.Date date8 = sdf8.parse("2012-01-01 00:00:00");
//        System.out.println(date8.getTime());
    }

}
