package service.impl;

import com.xinhong.ftp.util.UploadStatus;
import com.xinhong.sftp.util.SFtpUploadUtil;
import org.apache.log4j.Logger;
import service.UpService;

/**
 * Created by Administrator on 2018/4/26.
 */
public class UpServiceimpl implements UpService {
    static final Logger logger=Logger.getLogger(Logger.class);
    public UpServiceimpl() {
    }

    public UploadStatus upJob(String localFile, String remoteFile) {
        SFtpUploadUtil sFtpUploadUtil = SFtpUploadUtil.getInstance();
        sFtpUploadUtil.connect();
        UploadStatus uploadStatus = sFtpUploadUtil.upload(localFile,remoteFile);
//        logger.error(uploadStatus);
        return uploadStatus;
    }
}
