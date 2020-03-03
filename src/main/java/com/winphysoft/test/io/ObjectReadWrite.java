package com.winphysoft.test.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutput;

public class ObjectReadWrite {
    public static void main(String... args) throws Exception {
        write();
        read();
    }

    private static void read() throws Exception{
        DataInputStream dis = new DataInputStream(new FileInputStream(new File("C:\\code\\1")));
        System.out.println(dis.readInt());
        System.out.println(dis.readUTF());
        dis.close();
    }

    private static void write() throws Exception {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("C:\\code\\1")));
        dos.writeInt(1);
        dos.writeUTF("write");
        dos.close();
    }
}
