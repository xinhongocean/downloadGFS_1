package com.xinhong.gfs.processor;//package com.xinhong.gfs.processor;
//
//import com.alibaba.fastjson.JSONObject;
//import com.xinhong.gfs.download.DownloadTask;
//import com.xinhong.gfs.download.DownloadTaskCenter;
//import com.xinhong.gfs.download.TaskOperation;
//import com.xinhong.mp.Publish;
//import com.xinhong.util.*;
//import org.apache.log4j.Logger;
//
//import java.io.*;
//
//public class GfsParseProcess {
//    private static String osName = System.getProperties().getProperty("os.name");
//    //解释预报可执行程序路径
//    private static String smallFolderSign = "_folder";
//    private static String smallBinFolderSign = "_binSwapFolder";
//    private String gfsSmallFolder = "";
//    private String gfsSmallBinFolder = "";
//    private static final Logger logger = Logger.getLogger(GfsParseProcess.class);
//
//    private String path;
//    private DownloadTask task;
//
//    public GfsParseProcess(DownloadTask task) {
//        this.task = task;
//        this.path = task.getLocal();
//    }
//
//    private String strYMDH;
//    private String vti;
//
//    public void run() {
//        System.out.println("start to del");
//        delPath();
//        System.out.println("finish to del");
//        try {
//            //200秒之后再开始处理文件，防止idx文件下载不全
//            String idxPath = "";
//            String gfsPath = "";
//            if (path.lastIndexOf(".idx") > -1) {
//                idxPath = path;
//                gfsPath = idxPath.replace(".idx", "");
//            } else {
//                idxPath = path + ".idx";
//                gfsPath = path;
//            }
//            File idxF = new File(idxPath);
//            File gfsF = new File(gfsPath);
//            gfsPath = gfsPath.replace("\\", "/");
//            int idx0 = gfsPath.lastIndexOf("/");
//            strYMDH = gfsPath.substring(0, idx0);
//            idx0 = strYMDH.lastIndexOf("/");
//            strYMDH = strYMDH.substring(idx0 + 1, strYMDH.length());
//            int idx = gfsPath.lastIndexOf(".");
//            vti = gfsPath.substring(idx + 2, idx + 5);
//            //文件
//            if (idxF.exists() && gfsF.exists()) if (fliterGfs(idxF, gfsF)) {
//                try {
//                    //调用代码redis数据库
//                    logger.info("开始调用加载至redis数据库程序，folder=" + gfsSmallFolder);
//                    String strYMD = strYMDH.substring(0, 8);
//                    String strHH = strYMDH.substring(8);
//                    JSONObject parmJson = new JSONObject();
//                    parmJson.put("filePath", gfsSmallFolder);
//                    parmJson.put("dateStr", strYMD);
//                    parmJson.put("hour", strHH);
//                    parmJson.put("vti", vti);
//                    Publish.pub(parmJson.toString(), ConfigUtil.getProperty("gfs_apollo_topic"), true, Publish.DestinationEnum.QUEUE);
//                    //生成测试文件需注释
//                } catch (Exception e) {
//                    logger.error("调用代码加载至redis数据库出错，file = " + path);
//                    logger.error(e);
//                }
//
//                if (ConfigUtil.getBoolean(ConfigCommon.JSYB_CREATE)) {
//                    logger.info("文件解析完成，开始转换成二进制dat文件，file = " + path);
//                    gfsSmallBinFolder = gfsSmallFolder.replace(smallFolderSign, smallBinFolderSign);
//                    GribTranferr tranfer = new GribTranferr();
//                    boolean bl = tranfer.grbFolderToTxt(gfsSmallFolder, gfsSmallBinFolder);
//                    if (bl) {
//                        logger.info("二进制数据转换完成，file=" + gfsPath);
//                        logger.info("二进制转换完成，调用解释预报，file=" + gfsPath);
//                        //二进制转换完成，调用解释预报
//                        synchronized (this) {
//                            makeJSYB();
//                        }
//                    } else {
//                        logger.error("grb转换二进制文件出错，file=" + gfsPath);
//                    }
//                } else {
//                    logger.info("68 jsyb 暂停 ，检查配置文件，查看jsyb是否需要生成");
//                }
//            } else {
//                if (!idxF.exists()) {
//                    logger.error("索引文件不存在，idxFile = " + idxF.getName());
//                }
//                if (!gfsF.exists()) {
//                    logger.error("GRIB文件不存在，File = " + gfsF.getName());
//                }
//            }
//        } catch (Exception e) {
//            logger.error("大文件解析小文件过程出错：" + e.getMessage());
//            logger.error("文件名称 : " + path);
//        }
//
//
//    }
//
//
//    public static void delPath() {
//        try {
//            String currdateDate = DateUtil.getCurrentDate();
//            String fileDate = DateUtil.dateAddHour(currdateDate, -8);
//            String[] dateAry = fileDate.split(" ");
//            String[] ymd = dateAry[0].split("-");
//            String year = ymd[0];
//            String month = ymd[1];
//            String day = ymd[2];
//            String hour = dateAry[1];
//            String HH = getHH(hour);
//            for (int i = 6; i < 18; i++) {
//                String ymdhDate = year + "-" + month + "-" + day + " " + HH;
//                String dateBefore3days = DateUtil.dateAddHour(ymdhDate, -12 * i);
//                dateBefore3days = dateBefore3days.replace(" ", "").replace("-", "");
//                String dateBefore3daysPath = FtpConfig.getLocalPath() + "/" + dateBefore3days;
//                logger.info("删除目录下的子目录，保留数据文件," + dateBefore3daysPath);
//                FileHandler.deleteSubDir(dateBefore3daysPath);
//                String dateBefore3daysJSYBPath = ConfigUtil.getProperty(ConfigCommon.JSYB_DATAPATH)
//                        + "/" + dateBefore3days;
//                FileHandler.deleteDir(dateBefore3daysJSYBPath);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//
//    //解释预报
//    synchronized void makeJSYB() {
//        try {
//            String cmd = ConfigUtil.getProperty(ConfigCommon.JSYB_OUTFILE_PATH) + " " + strYMDH + " " + vti;
//            System.out.println("开始执行：ps = Runtime.getRuntime().exec(cmdAry);");
//            System.out.println(cmd);
//            Process ps = Runtime.getRuntime().exec(cmd);
//            try {
//                ps.waitFor();
//                logger.error("InputStream errorStream = ps.getErrorStream();");
//                InputStream errorStream = ps.getErrorStream();
//                testConsole(errorStream);
//                logger.error("InputStream inputStream = ps.getInputStream();");
//                InputStream inputStream = ps.getInputStream();
//                testConsole(inputStream);
//                //// TODO: 2016/10/20 判断解释预报文件是否生成成功
//                String path = ConfigUtil.getProperty(ConfigCommon.JSYB_DATAPATH);
//                path = path + "/" + strYMDH + "/XHGFS_G_TI_" + strYMDH + vti + ".dat";
//                File file = new File(path);
//                if (file.exists()) {
//                    //解释预报文件生成成功
//                    DownloadTaskCenter.notifyDownloadStatus(task, TaskOperation.Delete);
//                    JSONObject object = new JSONObject();
//                    object.put("name", "JSYB制作");
//                    object.put("msg", "正常");
//                    object.put("level", "INFO");
//                    object.put("ip", DownloadUtil.localIP);
//                    object.put("date", DateUtil.getCurrentDate_YMDHMS());
//                    Publish.pub(object.toString(), ConfigUtil.getProperty("monitor_apollo_topic"), true, Publish.DestinationEnum.QUEUE);
//                } else {
//                    DownloadTaskCenter.notifyDownloadStatus(task, TaskOperation.Delete);
//                    //解释预报文件生成失败
//                    JSONObject object = new JSONObject();
//                    object.put("name", "JSYB制作");
//                    object.put("msg", "失败");
//                    object.put("level", "ERROR");
//                    object.put("ip", "192.168.1.114");
//                    object.put("date", DateUtil.getCurrentDate_YMDHMS());
//                    Publish.pub(object.toString(), ConfigUtil.getProperty("monitor_apollo_topic"), true, Publish.DestinationEnum.QUEUE);
//                }
//                //填写完毕
//            } catch (InterruptedException e) {
//                logger.error("Thread.sleep(2 * 60 * 1000)出错");
//            }
//        } catch (IOException e) {
//            String sOut = "";
//            StackTraceElement[] trace = e.getStackTrace();
//            for (StackTraceElement s : trace) {
//                sOut += "\t at " + s + "\r\n";
//            }
//            System.out.println(sOut);
//        }
//    }
//
//    //拆小文件
//    private synchronized boolean fliterGfs(File idxF, File gfsF) {
//        try {
//            InputStream input = new FileInputStream(gfsF);
//            BufferedReader reader = new BufferedReader(new FileReader(idxF));
//            int fileIndex = 0;
//            String filename = "";
//            int start = 0, end = 0;
//            String s;
//            while ((s = reader.readLine()) != null) {
//                int idx = s.indexOf(":");
//                if (idx > 0) {
//                    fileIndex = Integer.parseInt(s.substring(0, idx));
//                }
//                int idx1 = s.indexOf(":", fileIndex + 1);
//                if (idx1 > 0) {
//                    start = Integer.parseInt(s.substring(idx + 1, idx1));
//                }
//                //编辑小文件名 XHGFS_HH_G_2016071500024_0500.grb
//                filename = getXHFilename(s);
//                break;
//
//            }
//
//            while ((s = reader.readLine()) != null) {
//                int idx = s.indexOf(":");
//                if (idx > 0) {
//                    fileIndex = Integer.parseInt(s.substring(0, idx));
//                }
////                if(fileIndex == 10){
////                    System.out.println(fileIndex);
////                }
//                int idx1 = s.indexOf(":", idx + 1);
//                if (idx1 > 0) {
//                    end = Integer.parseInt(s.substring(idx + 1, idx1));
//                }
//                if (filename != null && filename.length() > 0) {
//                    gfsSmallFolder = gfsF.getPath() + smallFolderSign;
//                    String smallPath = gfsSmallFolder + "/" + filename;
//                    File smallFile = new File(smallPath);
//                    if (!smallFile.exists()) {
//                        if (!smallFile.getParentFile().exists()) {
//                            smallFile.getParentFile().mkdirs();
//                        }
//                        smallFile.createNewFile();
//                    }
//                    OutputStream searchOsw = new FileOutputStream(smallFile);
//                    byte[] data = new byte[end - start];
//                    input.read(data);
//                    searchOsw.write(data);
//                    searchOsw.close();
//                } else {
//                    input.skip(end - start);
//                }
//                filename = getXHFilename(s);
//                start = end;
//            }
//            //最后一个文件
//            if (filename != null && filename.length() > 0) {
//                String smallPath = gfsF.getPath() + "_folder/" + filename;
//                File smallFile = new File(smallPath);
//                if (!smallFile.exists()) {
//                    if (!smallFile.getParentFile().exists()) {
//                        smallFile.getParentFile().mkdirs();
//                    }
//                    smallFile.createNewFile();
//                }
//                OutputStream searchOsw = new FileOutputStream(smallFile);
//                byte[] data = new byte[(int) (gfsF.length() - start)];
//                input.read(data);
//                searchOsw.write(data);
//                searchOsw.close();
//                input.close();
//                reader.close();
//            }
//            logger.info("文件解析完成，file = " + gfsF.getName());
//            return true;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            logger.error("文件解析出错，file = " + gfsF.getName());
//            logger.error("错误信息 = " + e.getMessage());
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("文件解析出错，file = " + gfsF.getName());
//            logger.error("错误信息 = " + e.getMessage());
//            return false;
//        }
//    }
//
//    //编辑小文件名 XHGFS_G_HH_0500_2016071500024.grb
//    //s=392:218490265:d=2016063012:RH:0.995 sigma level:1 hour fcst:
//    private synchronized String getXHFilename(String s) {
//        if (s == null || s.length() < 0)
//            return null;
//        String[] ary = s.split(":");
//        if (ary.length < 5) {
////            logger.error("所输入的idx文件中的文件名格式不正确，s=" + s);
//            return null;
//        }
//        String elem = ary[3].trim().toUpperCase();
//        String level = ary[4].trim().replace(" ", "").toLowerCase();
//        String key = elem + "_" + level;
//        if (ElemLevel.getElemMap().containsKey(key)) {
//            String filename = "XHGFS_G_" +
//                    ElemLevel.getElemMap().get(key) +
//                    "_" + strYMDH + vti + ".grb";
//            return filename;
//        } else {
////            logger.info("所输入的idx文件中的文件名要素和层次不在配置之内，s=" + s);
//            return null;
//        }
//    }
//
//    private void testConsole(InputStream ins) {
//        try {
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = -1;
//            while ((len = ins.read(buffer)) != -1) {
//                outStream.write(buffer, 0, len);
//            }
//            outStream.close();
//            ins.close();
//            logger.info(outStream.toString());
//            logger.info(new String(outStream.toByteArray()));
//        } catch (IOException ioe) {
//            logger.info("testConsole error");
//        }
//    }
//
//
//    private static String getHH(String hour) {
//        String HH = null;
//        switch (hour) {
//            case "04":
//            case "05":
//            case "06":
//            case "07":
//            case "08":
//            case "09":
//            case "10":
//            case "11":
//            case "12":
//            case "13":
//            case "14":
//            case "15":
//                HH = "00";
//                break;
//            case "16":
//            case "17":
//            case "18":
//            case "19":
//            case "20":
//            case "21":
//            case "22":
//            case "23":
//            case "00":
//            case "01":
//            case "02":
//            case "03":
//                HH = "12";
//                break;
//        }
//        return HH;
//    }
//
//}
