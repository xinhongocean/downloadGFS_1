package test;

import com.alibaba.fastjson.JSONObject;
import com.xinhong.mp.Publish;
import com.xinhong.util.ConfigUtil;
import commen.tools.ConfigDownload;

/**
 * Created by Administrator on 2018/4/27.
 */
public class sendmsg_test {
    public sendmsg_test() {
    }

    public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        obj.put("id", "up");
        obj.put("type", "gfs");
        obj.put("ip", ConfigDownload.getApollo_host());
        obj.put("date", "20180427");
        obj.put("content", ConfigDownload.getUpDataPath());
        String msg = obj.toJSONString();
        Publish publish = new Publish();

        publish.pub(msg,ConfigDownload.getGfs_apollo_topic(),true, Publish.DestinationEnum.QUEUE);
//        publish.pub(msg,"app.gfs.down",false, Publish.DestinationEnum.QUEUE);
        System.out.println();
    }
}
