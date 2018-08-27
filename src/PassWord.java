import com.fr.stable.CodeUtils;

import java.util.Date;

/**
 * Created by 小灰灰 on 2016/12/9.
 */
public class PassWord {
    public static void main(String[] args) {
        System.out.println(CodeUtils.passwordDecode("___0072002a00670066000a"));
        System.out.println(CodeUtils.passwordEncode("T_CUST_EMPextract"));
        System.out.println(new Date().toString());
    }
}
