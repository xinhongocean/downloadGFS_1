package test;

import com.xinhong.ftp.util.UploadStatus;
import commen.utils.DeleteFileUtil;
import service.UpService;
import service.impl.DownloadAndUpServiceimpl;
import service.impl.UpServiceimpl;

/**
 * Created by Administrator on 2018/4/27.
 */
public class up_test {
    public up_test() {
    }

//修改本地路径
    public static void main(String[] args) {
        DownloadAndUpServiceimpl downloadAndUpServiceimpl = new DownloadAndUpServiceimpl();
        downloadAndUpServiceimpl.doJob();
    }
}
