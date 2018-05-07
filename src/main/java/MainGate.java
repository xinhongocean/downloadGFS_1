import commen.tools.ConfigDownload;
import controller.DownloadController;

/**
 * Created by Administrator on 2018/4/28.
 */
public class MainGate {
    public MainGate() {

    }

    public static void main(String[] args) {
        new ConfigDownload();
        DownloadController.doJob();
    }
}
