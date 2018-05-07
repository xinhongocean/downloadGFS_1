package service.impl;

import commen.tools.LinuxCommander;
import org.apache.log4j.Logger;
import service.DownloadService;

/**
 * Created by Administrator on 2018/4/26.
 */
public class DownloadServiceimpl implements DownloadService {
    static final Logger logger=Logger.getLogger(Logger.class);
    public DownloadServiceimpl() {
    }

//    public static String relative = DownloadDaoimpl.class.getClassLoader().
//            getResource("").getPath();
    public  Boolean downloadJob(String exeName) {
        String cmd = "sh "  + "/home/app/gfs/gfssingle.sh" + " " + exeName;
//        String cmd = "wget http://ftp.ncep.noaa.gov/data/nccf/com/gfs/prod/gfs.2018050300/gfs.t00z.pgrb2.0p25.f000";
        System.out.println(cmd);
        //下载数据
        boolean flag = LinuxCommander.exeErrorCmd(cmd, null);
        logger.error(flag);
        return flag;
    }
}
