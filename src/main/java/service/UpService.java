package service;

import com.xinhong.ftp.util.UploadStatus;

/**
 * Created by Administrator on 2018/4/26.
 */
public interface UpService {
    UploadStatus upJob(String localFile, String remoteFile);
}
