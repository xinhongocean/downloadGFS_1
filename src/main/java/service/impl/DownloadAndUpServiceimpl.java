package service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xinhong.ftp.util.UploadStatus;
import com.xinhong.mp.Publish;
import commen.tools.ConfigDownload;
import commen.utils.DeleteFileUtil;
import dao.DownloadDao;
import dao.impl.DownloadDaoimpl;
import service.DownloadService;
import service.UpService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2018/4/25.
 */

public class DownloadAndUpServiceimpl{
    public static DownloadDao downloadDao;
    public static UpService upService;
    public  static DownloadService downloadService;
    public  static String[] hours;
    private static DecimalFormat decimalFormat00 = new DecimalFormat("00");
    private static DecimalFormat decimalFormat0000 = new DecimalFormat("0000");
    private static String date;

    public DownloadAndUpServiceimpl() {
        this.downloadDao = new DownloadDaoimpl();
        this.upService = new UpServiceimpl();
        this.downloadService = new DownloadServiceimpl();
        hours = downloadDao.getHour(Integer.parseInt(ConfigDownload.getDownHour()));
    }
    public DownloadAndUpServiceimpl(DownloadDao downloadDao, UpService upService, DownloadService downloadService) {
        this.downloadDao = downloadDao;
        this.upService = upService;
        this.downloadService = downloadService;
    }


    List<String> hours_list = new ArrayList<String>(); //先只满足重复一次的
    int overNum = 0;
    public void doJob() {
        date = getDate();
        Boolean flag;//////////////////////////////////////////////////////////////
        Data data = new Data();
        //下载线程
        Download_line download_line = new Download_line(data);
        Thread thread_download = new Thread(download_line);
        thread_download.start();
        //上传线程
        Upload_line upload_line = new Upload_line(data);
        Thread thread_upload = new Thread(upload_line);
        thread_upload.start();

    }



    //下载文件地址
    public static String getlocalFile(String exeName){
        String path = null;
        if(date.endsWith("00"))
        path = ConfigDownload.getDownloadDataPath()+"/"+date+"/"+ConfigDownload.getFileName_part00()+exeName;
        else path = ConfigDownload.getDownloadDataPath()+"/"+date+"/"+ConfigDownload.getFileName_part12()+exeName;
        return path;
    }
    public static String getlocalDir( ){
        String path = null;
        path = ConfigDownload.getDownloadDataPath()+"/"+date;
        return path;
    }
    //上传文件地址
    public static String getremoteFile(String exeName ){
        String path = null;
        if(date.endsWith("00"))
        path = ConfigDownload.getUpDataPath()+"/"+date+"/"+date+"_"+ConfigDownload.getFileName_part00()+exeName;
        else
        path = ConfigDownload.getUpDataPath()+"/"+date+"/"+date+"_"+ConfigDownload.getFileName_part12()+exeName;
        return path;
    }
    //根据shell描述，预报时间规定12点前日期为前一天日期+12，过了12点为当天日期+00
    private static String getDate(){
        Calendar calendar=Calendar.getInstance();
        int chour=calendar.get(Calendar.HOUR_OF_DAY);
        String yubao = null;
        if(chour <11) {calendar.setTimeInMillis(calendar.getTimeInMillis()-24*3600*1000);yubao = "12";
        }else yubao = "00";
        int cyear=calendar.get(Calendar.YEAR);
        int cmonth=calendar.get(Calendar.MONTH)+1;
        int cday=calendar.get(Calendar.DAY_OF_MONTH);
        return new String(decimalFormat0000.format(cyear)+decimalFormat00.format(cmonth)+decimalFormat00.format(cday)+yubao);
    }

    public static void sendMassage(String exeName){
        JSONObject obj = new JSONObject();
        obj.put("id", "up");
        obj.put("type", "gfs");
        obj.put("ip", ConfigDownload.getApollo_host());
        obj.put("date", date);
        obj.put("path", getremoteFile(exeName));
        String msg = obj.toJSONString();
        Publish publish = new Publish();
//        publish.pub(msg,ConfigDownload.getGfs_apollo_topic(),true, Publish.DestinationEnum.QUEUE);
        publish.pub(msg,"app.gfs.down",true, Publish.DestinationEnum.QUEUE);
        System.out.println("发送消息成功");
    }
}
/**
 * 下载（构造器为了调用引用变量（指针的作用），实现公用的对象data）
 */
class Download_line implements Runnable{
    Data data;
    DownloadService downloadService;
    String[] hours;
    int overNum = 0;
    public Download_line(Data data ){
        this.data=data;
        this.downloadService = DownloadAndUpServiceimpl.downloadService;  //这里需要先加载DownloadAndUpServiceimpl类
        this.hours = DownloadAndUpServiceimpl.hours;
    }
    public void run(){
//        while (true) {
            Boolean flag;
            for (String exeName : hours) {
                flag = false;                   //////////////////////////////////////////////////////////////
                System.out.println("开始下载  预报：" +exeName);
                flag = downloadService.downloadJob(Integer.toString(Integer.parseInt(exeName)));//////////////shell要求不是三位000
                if (flag.equals(true)) {
                    synchronized (data) {   //在run方法里上锁，包住需要独自执行的部分
//                        data.notifyAll();            //通信的唤醒
                        data.dataList_success.add(exeName);
//                        try {
//                            data.wait();         //通信的挂起
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                } else {
                    synchronized (data) {
                        data.dataList_fail.add(exeName);
                    }
                }
            }
            overDownload();
//        }
    }
    //对于下载失败的，重新下载一次
    public void overDownload(){
        overNum++;
        if(overNum<2 && !data.dataList_fail.isEmpty() ) {
            System.out.println("重新下载未完成文件");
            String[] hours = new String[data.dataList_fail.size()];
            for (int i = 0; i <data.dataList_fail.size() ; i++) {
                hours[i] = data.dataList_fail.get(i);
            }
            setHours(hours);
            synchronized (data) {
                data.dataList_fail.clear();
            }
            this.run();
        }
    }

    public String[] getHours() {
        return hours;
    }

    public void setHours(String[] hours) {
        this.hours = hours;
    }
}
/**
 * 上传
 */
class Upload_line implements Runnable {
    Data data;
    String exeName;
    UpService upService;
    public Upload_line(Data data){
        this.data=data;
        this.upService = DownloadAndUpServiceimpl.upService;
    }
    //根据上传的文件个数结束线程
    public void run(){
        int fileNum = 0;
        while (fileNum< Integer.parseInt(ConfigDownload.getDownHour())) {
            synchronized (data) {
                if (!data.dataList_success.isEmpty()) {
                    exeName = data.dataList_success.get(0);
                    data.dataList_success.remove(0);
                }else exeName = "";
            }
            if (exeName .equals("")){
                try {
                    Thread.sleep(1000*30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            String localFile = DownloadAndUpServiceimpl.getlocalFile(exeName);  //本地文件目录
            String localDir = DownloadAndUpServiceimpl.getlocalDir();  //本地文件目录
            String remoteFile = DownloadAndUpServiceimpl.getremoteFile(exeName);    //远程文件目录
            UploadStatus uploadStatus = null;
            UploadStatus uploadStatus_idx = null; //上传对应的idx文件
            //上传数据
// TODO: 2018/4/27  //上传失败，如何？
            System.out.println("开始上传");
            System.out.println(localFile+"   shangchuan: "+remoteFile);
            uploadStatus = upService.upJob(localFile,remoteFile);
            uploadStatus_idx = upService.upJob(localFile+".idx",remoteFile+".idx");

            //发送队列消息
            //删除本地文件
            System.out.println("发送消息和删除文件");
            if(uploadStatus.equals(UploadStatus.Upload_New_File_Success) ){
                DownloadAndUpServiceimpl.sendMassage(exeName);
                DeleteFileUtil.delete(localFile);
                fileNum++;
            }
            if(uploadStatus_idx.equals(UploadStatus.Upload_New_File_Success) ){
                DeleteFileUtil.delete(localFile+".idx");
            }
//            if(exeName.equals(hours[hours.length - 1]) && uploadStatus.equals(UploadStatus.Upload_New_File_Failed)){
//                DeleteFileUtil.delete(localDir);
//            }else if(exeName.equals(hours[hours.length - 1])){
//                DownloadAndUpServiceimpl.sendMassage(exeName);
//                DeleteFileUtil.delete(localDir);
//            }else {
//                DownloadAndUpServiceimpl.sendMassage(exeName);
//                DeleteFileUtil.delete(localFile);
//            }
        }
    }
}

/**
 * 共享数据类，方便调用共享对象和数据
 */
class Data {
    List<String> dataList_success = new ArrayList<String>();
    List<String> dataList_fail = new ArrayList<String>();
}
