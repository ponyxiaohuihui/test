package com.huihui.jpython.jep;

import jep.DirectNDArray;
import jep.Jep;
import jep.JepConfig;
import jep.MainInterpreter;
import jep.PyConfig;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class JepNDArrayTest {
    public static void main(String[] args) throws Exception{
        PyConfig pyConfig = new PyConfig();
        pyConfig.setPythonHome("C:\\Program Files\\Python");
        MainInterpreter.setInitParams(pyConfig);
        JepConfig jepConfig = new JepConfig();
        Jep jep = new Jep(jepConfig);
        jepConfig.setRedirectOutputStreams(true);
        jep.eval("from java.lang import System");
        jep.eval("s = 'Hello World'");
        jep.eval("System.out.println(s)");
        jep.eval("print(s)");
        jep.eval("print(s[1:-1])");
        jep.eval("import numpy as np");
        jep.eval("a = np.array([1,2,3])");
        jep.eval("System.out.println(str(a))");

        Thread.sleep(1000*10);
        System.out.println("1");
        // 测试内存共享
        int dimNum = 200000000;
        double[] xxx = new double[dimNum];
        for (int i = 0; i < dimNum; i++) {
            xxx[i] = i;
        }
        Thread.sleep(1000*10);
        System.out.println("2");
        DoubleBuffer data = ByteBuffer.allocateDirect(dimNum * 8).asDoubleBuffer();
        DirectNDArray<DoubleBuffer> nd = new DirectNDArray<>(data, dimNum);
        jep.set("x", nd);
        jep.eval("x[:] = 700");
        // val will 700 since we set it in python
        double val = data.get(1);
        data.put(2, val + 100);
        // prints 800 since we set in java
        jep.eval("print(x[2])");
        Thread.sleep(1000000000);

        // 方式一，每行单独执行
        jep.eval("from sklearn import datasets");
        jep.eval("from sklearn.model_selection import train_test_split");
        jep.eval("from sklearn.neighbors import KNeighborsClassifier");
        jep.eval("iris=datasets.load_iris()");
        jep.eval("iris_X=iris.data");
        jep.eval("iris_y=iris.target");
        jep.eval("X_train,X_test,y_train,y_test=train_test_split(iris_X,iris_y,test_size=0.3)");
        jep.eval("print(y_train)");

        // 方式二，函数包装功能
        String evalStr = "def aaa():\n" +
                "   from sklearn import datasets\n" +
                "   from sklearn.model_selection import train_test_split\n" +
                "   from sklearn.neighbors import KNeighborsClassifier\n" +
                "   iris=datasets.load_iris()\n" +
                "   iris_X=iris.data\n" +
                "   iris_y=iris.target\n" +
                "   X_train,X_test,y_train,y_test=train_test_split(iris_X,iris_y,test_size=0.3)\n" +
                "   print(y_train)";
        jep.eval(evalStr);
        jep.eval("aaa()");


        //方式三，使用exec命令
        jep.set("_pythonScript", "from sklearn import datasets\n" +
                "from sklearn.model_selection import train_test_split\n" +
                "from sklearn.neighbors import KNeighborsClassifier\n" +
                "iris=datasets.load_iris()\n" +
                "iris_X=iris.data\n" +
                "iris_y=iris.target\n" +
                "X_train,X_test,y_train,y_test=train_test_split(iris_X,iris_y,test_size=0.3)\n" +
                "print(y_train)");
        jep.eval("exec(_pythonScript)");
        jep.eval("del(_pythonScript)");
        jep.close();
    }
}
