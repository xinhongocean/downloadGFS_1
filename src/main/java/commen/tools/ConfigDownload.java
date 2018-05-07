package commen.tools;
import java.io.*;
import java.util.Properties;

/**
 * Created by Administrator on 2018/4/13.
 */
public class ConfigDownload {
//conf.downConf.properties    配置文件信息
    static String downHour;
    static String single_maximum_time;
    static String single_ask_time;
    static String host;
    static String port;
    static String username;
    static String password;
//conf.upConf.properties    配置文件信息
    static String downloadDataPath;
    static String upDataPath;
    static String fileName_part00;
    static String fileName_part12;
//conf.haConf.properties    配置文件信息
    static String apollo_user;
    static String apollo_password;
    static String apollo_host;
    static String apollo_port;
    static String gfs_apollo_topic;

    static String inPath_JBDB;
    public ConfigDownload() {
    }

    static {
        loaddown();
        loadup();
        loadmsg();
    }

    private static void loaddown(){
        //通过类加载器加载配置文件
        Properties properties=new Properties();
        InputStream in = null;
        try {
            String springPath= System.getProperty("user.dir")+"/conf/downConf/downConf.properties";
//            String springPath= System.getProperty("user.dir")+"/src/main/resources/conf/downConf/downConf.properties";
//            String springPath= DownloadController.class.getResource("")+"conf/downConf/downConf.properties";
            in = new BufferedInputStream(new FileInputStream(springPath));
//            in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") +"/conf/downConf/downConf.properties"));
            properties.load(in);

            downHour=properties.getProperty("downHour");
            single_maximum_time=properties.getProperty("single_maximum_time");
            single_ask_time=properties.getProperty("single_ask_time");
            downloadDataPath=properties.getProperty("downloadDataPath");
            fileName_part00=properties.getProperty("fileName_part00");
            fileName_part12=properties.getProperty("fileName_part12");

            inPath_JBDB=properties.getProperty("inPath_JBDB");
//            isobaric_JBDB=properties.getProperty("isobaric_JBDB").split(",");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in !=null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void loadmsg(){
        //通过类加载器加载配置文件
        Properties properties=new Properties();
        InputStream in = null;
        try {

            String springPath= System.getProperty("user.dir")+"/conf/gfsConf/ha.properties";
//            String springPath= System.getProperty("user.dir")+"/src/main/resources/conf/gfsConf/ha.properties";
            in = new BufferedInputStream(new FileInputStream(springPath));
//            in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+ "/conf/gfsConf/ha.properties"));
            properties.load(in);
            apollo_user=properties.getProperty("apollo_user");
            apollo_password=properties.getProperty("apollo_password");
            apollo_host=properties.getProperty("apollo_host");
            apollo_port=properties.getProperty("apollo_port");
            gfs_apollo_topic=properties.getProperty("gfs_apollo_topic");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in!=null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void loadup(){
        //通过类加载器加载配置文件
        Properties properties=new Properties();
        InputStream in = null;
        try {
            String springPath=System.getProperty("user.dir")+"/conf/upConf/upConf.properties";
//            String springPath=System.getProperty("user.dir")+"/src/main/resources/conf/upConf/upConf.properties";
            in = new BufferedInputStream(new FileInputStream(springPath));
//            in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+ "/conf/upConf/upConf.properties"));
            properties.load(in);
            host=properties.getProperty("host");
            port=properties.getProperty("port");
            username=properties.getProperty("username");
            password=properties.getProperty("password");
            upDataPath=properties.getProperty("upDataPath");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in !=null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String getDownHour() {
        return downHour;
    }

    public static void setDownHour(String downHour) {
        ConfigDownload.downHour = downHour;
    }



    public static String getInPath_JBDB() {
        return inPath_JBDB;
    }

    public static void setInPath_JBDB(String inPath_JBDB) {
        ConfigDownload.inPath_JBDB = inPath_JBDB;
    }



    public static String getSingle_maximum_time() {
        return single_maximum_time;
    }

    public static void setSingle_maximum_time(String single_maximum_time) {
        ConfigDownload.single_maximum_time = single_maximum_time;
    }

    public static String getSingle_ask_time() {
        return single_ask_time;
    }

    public static void setSingle_ask_time(String single_ask_time) {
        ConfigDownload.single_ask_time = single_ask_time;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        ConfigDownload.host = host;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        ConfigDownload.port = port;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ConfigDownload.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        ConfigDownload.password = password;
    }

    public static String getDownloadDataPath() {
        return downloadDataPath;
    }

    public static void setDownloadDataPath(String downloadDataPath) {
        ConfigDownload.downloadDataPath = downloadDataPath;
    }

    public static String getUpDataPath() {
        return upDataPath;
    }

    public static void setUpDataPath(String upDataPath) {
        ConfigDownload.upDataPath = upDataPath;
    }

    public static String getApollo_user() {
        return apollo_user;
    }

    public static void setApollo_user(String apollo_user) {
        ConfigDownload.apollo_user = apollo_user;
    }

    public static String getApollo_password() {
        return apollo_password;
    }

    public static void setApollo_password(String apollo_password) {
        ConfigDownload.apollo_password = apollo_password;
    }

    public static String getApollo_host() {
        return apollo_host;
    }

    public static void setApollo_host(String apollo_host) {
        ConfigDownload.apollo_host = apollo_host;
    }

    public static String getApollo_port() {
        return apollo_port;
    }

    public static void setApollo_port(String apollo_port) {
        ConfigDownload.apollo_port = apollo_port;
    }

    public static String getGfs_apollo_topic() {
        return gfs_apollo_topic;
    }

    public static void setGfs_apollo_topic(String gfs_apollo_topic) {
        ConfigDownload.gfs_apollo_topic = gfs_apollo_topic;
    }

    public static String getFileName_part00() {
        return fileName_part00;
    }

    public static void setFileName_part00(String fileName_part00) {
        ConfigDownload.fileName_part00 = fileName_part00;
    }

    public static String getFileName_part12() {
        return fileName_part12;
    }

    public static void setFileName_part12(String fileName_part12) {
        ConfigDownload.fileName_part12 = fileName_part12;
    }
}
