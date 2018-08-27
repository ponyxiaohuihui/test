package emun;

/**
 * Created by 小灰灰 on 2017/4/18.
 */
public class A extends AbstractC {
    @Override
    byte getType() {
        return Type.ROARING_INDEX_All_SHOW.getType();
    }

    public static void main(String[] args) {
        I[] as = new I[10000000];
        for (int i = 0; i < as.length; i++){
            as[i] = new A();
        }
        long t = System.currentTimeMillis();
        for (int i = 0; i < as.length; i++){
            if ( ((AbstractC)as[i]).getType() != Type.ROARING_INDEX_All_SHOW.getType()){
                System.out.println("bbb");
            }
        }
        System.out.println(System.currentTimeMillis() - t);
    }
}


