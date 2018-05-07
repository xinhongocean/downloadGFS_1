package dao.impl;

import dao.DownloadDao;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/4/25.
 */
public class DownloadDaoimpl implements DownloadDao{
    public DownloadDaoimpl() {
    }
    DecimalFormat decimalFormat = new DecimalFormat("000");
    //根据小时大小，下载01~hour的数据
    @Override
    public String[] getHour(int hour) {
        String[] hours=null;
        if(hour>0){
             hours = new String[hour];
            for (int i = 0; i <hour ; i++) {
                hours[i] = decimalFormat.format(i);
            }
        }
        return hours;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }
}
