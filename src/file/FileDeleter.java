package file;

import java.io.File;

/**
 * Created by pony on 2018/9/12.
 */
public class FileDeleter {
    public static void main(String[] args) {
        File f = new File("C:\\Users\\zihai\\Desktop\\02e00a80-a2ce-11e8-9f7b-a8583b69db09_X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A_20180911_us-east-1_s3_aws4_request&X-Amz-Date=20180911T073316Z&X-Amz-Expires=300&X-Amz-Signature=6429427260664d095cc92db723a00054058a2fe2fb5b64.txt");
        f.delete();
    }
}
