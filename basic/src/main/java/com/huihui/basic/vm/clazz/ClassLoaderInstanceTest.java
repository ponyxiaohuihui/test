package com.huihui.basic.vm.clazz;

import java.io.InputStream;

public class ClassLoaderInstanceTest {

    public static void main(String[] args) throws Exception{
        Object ob = new Loader().loadClass("com.huihui.test.cls.ClassLoaderInstanceTest").newInstance();
        System.out.println(ob.getClass());
        System.out.println(ob instanceof ClassLoaderInstanceTest);
    }

    static class Loader extends ClassLoader{
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            InputStream is = getClass().getResourceAsStream("ClassLoaderInstanceTest.class");
            byte[] bytes = new byte[0];
            try {
                bytes = new byte[is.available()];
                is.read(bytes);
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Exception e) {
                return super.loadClass(name);
            }
        }
    }
}
