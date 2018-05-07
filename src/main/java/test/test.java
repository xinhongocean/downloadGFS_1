package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinhong.mp.Publish;
import commen.tools.ConfigDownload;
import commen.tools.LinuxCommander;
import commen.utils.DeleteFileUtil;
import controller.DownloadController;
import dao.impl.DownloadDaoimpl;
import service.impl.DownloadAndUpServiceimpl;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/4/25.
 */
public class test {
    public test() {
    }
    private static DecimalFormat decimalFormat00 = new DecimalFormat("00");
    private static DecimalFormat decimalFormat0000 = new DecimalFormat("0000");
    public static void main(String[] args) {
        //简单间隔时间操作
//        Timer time = new Timer();
//        TimerTask task = new TimerTask() {
//            private int count = 10;
//            @Override
//            public void run() {
//
//            }
//        };
        Dataa dataa = new Dataa();
        Test_1 test_1 = new Test_1(dataa);
        Thread thread1 = new Thread(test_1);
        thread1.start();
//        Test_2 test_2 = new Test_2(dataa);
//        Thread thread2 = new Thread(test_2);
//        thread2.start();


    }

}
class Test_1 implements Runnable{
    Dataa a;
    public Test_1( Dataa a) {
        this.a = a;
    }

    @Override
    public void run() {
        synchronized (a){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            try {
//                a.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println("sleep 2 test1");
        }
    }
}
class Test_2 implements Runnable{
    Dataa a;
    public Test_2( Dataa a) {
        this.a = a;
    }

    @Override
    public void run() {
        synchronized (a){
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println("sleep 2 s test2");
        }
    }
}
class Dataa{
    int a = 1;
}
