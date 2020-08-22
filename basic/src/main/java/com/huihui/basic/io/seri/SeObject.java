package com.huihui.basic.io.seri;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class SeObject implements Serializable {

    private static final long serialVersionUID = 5672407479628071664L;

    private void writeObject(ObjectOutputStream stream) throws IOException{
        System.out.println("write");
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
        System.out.println("read");
    }

    public static void main(String[] args) throws Exception{
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("A.out"));
        SeObject ob = new SeObject();
        Method method = ob.getClass().getDeclaredMethod("writeObject", ObjectOutputStream.class);
        method.setAccessible(true);
        Field modifiers = method.getClass().getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(method, Modifier.PUBLIC);

        stream.writeObject(ob);
        stream.close();
    }
}
