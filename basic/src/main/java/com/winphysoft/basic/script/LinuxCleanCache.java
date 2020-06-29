package com.winphysoft.basic.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by 小灰灰 on 2017/6/1.
 */
public class LinuxCleanCache {
    //超过80G清除
    private static long MEM = 80000000;
    //十分钟查询一次
    private static long TIME = 600000;
    public static void main(String[] args){
        try{
            int arg = Integer.parseInt(args[0]);
            //大于4g才有效
            if (arg > 4){
                MEM = arg * 1000000;
                //防止越界
                if (MEM < 4000000){
                    MEM = 80000000;
                } else {
                    System.out.println("Memory is "+ arg + "G");
                }
            }
        } catch (Exception e){

        }
        try{
            int arg = Integer.parseInt(args[1]);
            //大于1min才有效
            if (arg > 0){
                TIME = arg * 60000;
                //防止越界
                if (TIME < 60000){
                    TIME = 600000;
                }else {
                    System.out.println("Time is "+ arg + "min");
                }
            }
        } catch (Exception e){

        }
        while (true){
            try {
                Process process = Runtime.getRuntime().exec("cat /proc/meminfo");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                long men = 0;
                String s = null;
                while ((s = reader.readLine()) != null){
                    if (s.startsWith("Cached")){
                        String[] results = s.split(" ");
                        men = Long.parseLong(results[results.length - 2]);
                        System.out.println("find " + men);
                        break;
                    }
                }
                if (men > MEM){
                    //这边直接exec虽然返回0，但是执行不成功，可能是权限的问题，加两个sh文件执行下吧
                    File f = new File("").getAbsoluteFile();
                    ProcessBuilder sync = new ProcessBuilder("./sync.sh");
                    sync.directory(f);
                    System.out.println("finish sync " + sync.start().waitFor());
                    ProcessBuilder release = new ProcessBuilder("./clearcache.sh");
                    release.directory(f);
                    System.out.println("finish release " + release.start().waitFor());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
