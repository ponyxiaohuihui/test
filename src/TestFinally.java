/**
 * Created by 小灰灰 on 2016/12/20.
 */
public class TestFinally {
    public static void main(String[] args) {
        System.out.println(getA().a);
    }
    private static S getA(){
        S s = new S();
        s.a = "a";
        try{
            return s;
        } catch (Exception e){

        }finally {
            s.a = "aaa";
        }
        return s;
    }

    static class S {
        String a;
    }
}
