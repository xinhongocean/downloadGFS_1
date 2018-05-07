package test;

import com.alibaba.fastjson.JSONObject;
import com.xinhong.mp.Publish;
import commen.tools.ConfigDownload;

/**
 * Created by Administrator on 2018/5/4.
 */
public class massage_test {
    public massage_test() {
    }
//需要复制下conf
    public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        obj.put("id", "up");
        obj.put("type", "gfs");
        obj.put("ip", ConfigDownload.getApollo_host());
        obj.put("date", "2018050400");
        obj.put("path", "test");
        String msg = obj.toJSONString();
        Publish publish = new Publish();
//        publish.pub(msg,ConfigDownload.getGfs_apollo_topic(),true, Publish.DestinationEnum.QUEUE);
        publish.pub(msg,"app.test",true, Publish.DestinationEnum.QUEUE);
        System.out.println("发送消息成功");
    }
}
