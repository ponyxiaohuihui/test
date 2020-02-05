package com.winphysoft.test.jocl;


import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Random;

import static java.lang.System.*;
import static java.lang.Math.*;
/**
 * @author pony
 * @version 1.1
 * Created by pony on 2019/12/27
 */
public class JOCLSample{

    public static void main(String[] args) throws IOException {



    }

    private static void fillBuffer(FloatBuffer buffer, int seed) {
        Random rnd = new Random(seed);
        while(buffer.remaining() != 0)
            buffer.put(rnd.nextFloat()*100);
        buffer.rewind();
    }

    private static int roundUp(int groupSize, int globalSize) {
        int r = globalSize % groupSize;
        if (r == 0) {
            return globalSize;
        } else {
            return globalSize + groupSize - r;
        }
    }

}