package bigdecimal;

import java.util.Arrays;

public class MultiPly {
    public static void main(String[] args) {
        System.out.println(multip("789", "769"));
    }

    public static String multip(String a, String b){
        int[] c = new int[a.length() + b.length()];
        Arrays.fill(c, 0);
        for (int i = b.length() - 1;i >=0; i--){
            for (int j = a.length() - 1; j >=0; j--){
               int index = j + i + 1;
               int value = (b.charAt(i) - 48) * (a.charAt(j) - 48);
               plus(c, index, value);
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i : c){
            sb.append(i);
        }
        return sb.toString();
    }

    private static void plus(int[] c, int index, int value) {
        for (int i = index; i>=0; i--){
           int v = c[i];
           int result = value + v;
           int mod = result % 10;
           value = result / 10;
           c[i] = mod;
           if (value == 0){
               break;
           }
        }

    }
}
