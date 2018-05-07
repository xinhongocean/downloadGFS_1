package controller;

import com.xinhong.util.ConfigUtil;
import commen.tools.DownloadTimeManger;
import service.DownloadService;
import service.impl.DownloadAndUpServiceimpl;

/**
 * Created by Administrator on 2018/4/26.
 */

public class DownloadController {
    public DownloadController() {
    }

    public static void doJob(){
        DownloadAndUpServiceimpl downloadAndUpServiceimpl = new DownloadAndUpServiceimpl();
        //直接测
        downloadAndUpServiceimpl.doJob();
        //加时间测
        DownloadTimeManger manger =
                new DownloadTimeManger(DownloadTimeManger.TIME_PER_DAY,
                        new String[]{"990400", "991200"},//04,12
                        downloadAndUpServiceimpl);
    }
}
