package com.winphysoft.basic.compare;

import sun.text.UCompactIntArray;

import java.lang.reflect.Field;
import java.text.CollationElementIterator;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Locale;

public class PinYinCompare {
    public static void main(String[] args) throws Exception{
        Collator collator = Collator.getInstance(Locale.CHINA);
        PinYinCompare array = new PinYinCompare();
        System.out.println(array.getOrder('x') - array.getOrder('X'));
        System.out.println(array.getOrder('安') - array.getOrder('韩'));
        System.out.println(array.getOrder('韩') - array.getOrder('陆'));
        System.out.println(array.getOrder('陆') - array.getOrder('张'));
        System.out.println(array.getOrder('张') - array.getOrder('钟'));
        System.out.println(collator.compare("x", "X"));
        System.out.println(collator.compare("安", "韩"));
        System.out.println(collator.compare("韩", "陆"));
        System.out.println(collator.compare("陆", "张"));
        System.out.println(collator.compare("张", "钟"));

    }
        UCompactIntArray array;
        public PinYinCompare() throws Exception{
            Collator collator = Collator.getInstance(Locale.CHINA);
            Field tableField = collator.getClass().getDeclaredField("tables");
            tableField.setAccessible(true);
            Object ob = tableField.get(collator);
            Class tableClass = Class.forName("java.text.RBCollationTables");

            Field mapField = tableClass.getDeclaredField("mapping");
            mapField.setAccessible(true);
            array = (UCompactIntArray) mapField.get(ob);
        }

        public int getOrder(int index){
            return CollationElementIterator.primaryOrder(array.elementAt(index));
        }

}
