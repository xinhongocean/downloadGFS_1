package commen.tools;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wingsby on 2018/3/20.
 */
public class LinuxCommander {
    static final Logger logger=Logger.getLogger(Logger.class);

    public LinuxCommander() {
    }

    public static List<String> getCMDMessage(String cmd){
        StringBuilder sb=new StringBuilder();
        List<String> list=new ArrayList<>();
        try {
            Process process=Runtime.getRuntime().exec(cmd);
            InputStreamReader ir=new InputStreamReader(process.getInputStream());
            BufferedReader input=new BufferedReader(ir);
            String line=null;
//            logger.info(cmd);
            while((line=input.readLine())!=null){
                sb.append(line);
                sb.append('\n');
                list.add(line);
//                logger.error(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

//执行绝对路径,input里面包含有消息的内容
    public  static boolean exeCMD(String cmd,List<String> resStr) {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());

            BufferedReader input = new BufferedReader(ir);
            String line = null;
//            logger.error(input);
            while ((line = input.readLine()) != null) {
                logger.error(line);
                sb.append(line);
                sb.append('\n');
                if(resStr!=null)resStr.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
//执行绝对路径,input里面包含有消息的内容
    public  static boolean exeErrorCmd(String cmd,List<String> resStr) {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getErrorStream());

            BufferedReader input = new BufferedReader(ir);
            String line = null;
//            logger.error(input);
            while ((line = input.readLine()) != null) {
//                logger.error(line);
                if(line .contains("save"))flag = true;
                sb.append(line);
                sb.append('\n');
                if(resStr!=null)resStr.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return flag;
    }
     public static boolean executeNewFlow(String cmd,List<String> resStr) {
         Runtime run = Runtime.getRuntime();
         Process proc = null;
         try {
             proc = run.exec(cmd);
         } catch (IOException e) {
             e.printStackTrace();
             return false;
         }
         if (proc != null) {
             BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);

             //执行的linux命令，（一般复杂流程可以1、shell脚本
             // 2、可以启动一个shell长连接，保持连接，发送多条命令，最后释放连接）
             out.println("cd /home/app/gfs");
//             out.println("rm -fr /home/proxy.log");
             out.println("exit");//这个命令必须执行，否则in流不结束。
             try {
                 String line;
                 while ((line = in.readLine()) != null) {
                     System.out.println(line);
                 }
                 proc.waitFor();
                 in.close();
                 out.close();
                 proc.destroy();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         return true;
     }
}
