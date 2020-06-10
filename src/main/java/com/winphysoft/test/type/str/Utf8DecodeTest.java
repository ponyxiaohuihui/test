package com.winphysoft.test.type.str;

import java.nio.charset.CodingErrorAction;

/**
 * @author pony
 * @version 1.1
 * Created by pony on 2020/6/8
 */
public class Utf8DecodeTest {
    private static char REPLACEMENT = "\uFFFD".charAt(0);
    public static void main(String[] args) throws Exception {
        String s = "1哈\uD800\uFFFF\uEFFF";
        byte[] bytes = s.getBytes("UTF-8");
        String ss = new String(bytes, "UTF-8");
        char char1 = utf8Decode(bytes, 0, bytes.length);
        char char2 = utf8Decode(bytes, 1, bytes.length);

        System.out.println(ss);
    }

    private static char utf8Decode(byte[] bytes, int start, int maxLen) {
        byte b0 = bytes[start];
        if (b0 < 0) {
            //不是110开头的或者是 10xxxxxx,或者是11000001（128的开头是11000010，比这个小的应该都是一位的） 可能为3位或者4位utf8
            if (b0 >> 5 != -2 || (b0 & 30) == 0){
                if (b0 >> 4 == -2){
                    //1110xxxx 三位的情况
                    if (start + 2 < maxLen){
                        byte b1 = bytes[start + 1];
                        byte b2 = bytes[start + 2];
                        if (isMalformed3(b0, b1, b2)) {
                            //格式不对的情况,算下需要往后移几位
                            return REPLACEMENT;
                        } else {
                            char c = (char)(b0 << 12 ^ b1 << 6 ^ b2 ^ -123008);
                            if (Character.isSurrogate(c)) {
                                return REPLACEMENT;
                            } else {
                                return c;
                            }
                        }
                    } else {
                        return REPLACEMENT;
                    }


                } else if (b0 >> 3 != -2){
                    //非11110xxx
                    return REPLACEMENT;
                } else if (start + 3 < maxLen){
                    //长度足够四位的情况的情况
                    byte b1 = bytes[start + 1];
                    byte b2 = bytes[start + 2];
                    byte b3 = bytes[start + 3];
                    int twoChar = b0 << 18 ^ b1 << 12 ^ b2 << 6 ^ b3 ^ 3678080;
                    if (!isMalformed4(b1, b2, b3) && Character.isSupplementaryCodePoint(twoChar)) {
                        char high = Character.highSurrogate(twoChar);
                        char low = Character.lowSurrogate(twoChar);
                        return high;
                    } else {
                        //格式不对的情况,算下需要往后移几位
                        return REPLACEMENT;
                    }
                } else {
                    //为11110xxx但是长度不够4位的情况,处理截断的情况，这里jdk写的有点乱，可以不用考虑
                    return REPLACEMENT;
                }

            } else {
                //110开头的utf8 110xxxxx 10xxxxxx
                if (start + 1 > maxLen){
                    return REPLACEMENT;
                }
                byte b1 = bytes[start+1];
                if (isMalformed2(b1)) {
                   return REPLACEMENT;
                } else {
                    return (char)(b0 << 6 ^ b1 ^ 3968);
                }

            }
        } else {
            return (char) b0;
        }
    }

    //&11000000判断是不是10开头的
    private static boolean isMalformed2(int b1) {
        return (b1 & 192) != 128;
    }

    //4位utf8后3位是不是都是11000000开头
    private static boolean isMalformed4(int b1, int b2, int b3) {
        return (b1 & 192) != 128 || (b2 & 192) != 128 || (b3 & 192) != 128;
    }

    //第一位是11100000或者第一位是1110xxxx，第二或者第三位不是11000000
    private static boolean isMalformed3(int b0, int b1, int b2) {
        return b0 == -32 && (b0 & 224) == 128 || (b1 & 192) != 128 || (b2 & 192) != 128;
    }

}
