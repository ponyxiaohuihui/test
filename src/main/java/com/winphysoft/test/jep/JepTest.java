package com.winphysoft.test.jep;

import jep.Jep;
import jep.JepConfig;
import jep.MainInterpreter;
import jep.NDArray;
import jep.PyConfig;

import java.util.ArrayList;
import java.util.List;

public class JepTest {
    public static void main(String[] args) throws Exception{
        PyConfig pyConfig = new PyConfig();
        pyConfig.setPythonHome("C:\\Program Files\\Python");
        MainInterpreter.setInitParams(pyConfig);
        JepConfig jepConfig = new JepConfig().addSharedModules("numpy");
        Jep jep = new Jep(jepConfig);
        Object[][] values = new Object[][]{{"i","aa","bb","cc"},{1,2,3,4}};
        NDArray ndArray = new NDArray(new int[]{1, 2,3,4}, 2,2);
        jep.set("a", values);
        jep.set("d", ndArray);
        jep.runScript("C:\\codes\\pydemo\\jeptest.py");
        System.out.println(jep.getValue("b"));
        System.out.println(jep.getValue("c"));
        List<Long> list = new ArrayList<>();
        jep.close();
    }
}
