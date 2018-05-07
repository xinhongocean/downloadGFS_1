package commen.tools;


import service.DownloadService;
import service.impl.DownloadAndUpServiceimpl;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wingsby on 2018/4/16.
 */
public class DownloadTimeManger {
    public static final int TIME_PER_DAY=0;
    public static final int TIME_PER_WEEK=1;
    public static final int TIME_PER_MONTH=2;
    public static final int UNFIXED_TIME=9;

    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000l;
    private static final long PERIOD_HOUR = 60 * 60 * 1000l;
    private static final long PERIOD_WEEK = 7*24 * 60 * 60 * 1000l;
    private static final long tolerance=50*60*1000l;   /////////////////////////////////////////////////

    public static int mode;
    private static String[] dates;
//    private static final long testp = 61 * 1000l;

    public DownloadTimeManger(){

    }

    //dates:DDHHMM
    public  DownloadTimeManger(final int mode, String[] dates, final DownloadAndUpServiceimpl downloadAndUpServiceimpl){
        this.mode=mode;//不这么写一直有bug
        this.dates = dates;
            TimerTask timerTask=new TimerTask() {
                @Override
                public void run() {
                    Calendar calendar=Calendar.getInstance();
                    int cyear=calendar.get(Calendar.YEAR);
                    int cmonth=calendar.get(Calendar.MONTH)+1;
                    int cday=calendar.get(Calendar.DAY_OF_MONTH);
                    int chour=calendar.get(Calendar.HOUR_OF_DAY);
                    for(String str: DownloadTimeManger.dates){
                        int DAY=Integer.valueOf(str.substring(0,2));
                        int hour=Integer.valueOf(str.substring(2,4));
                        int minute=Integer.valueOf(str.substring(4,6));
                        switch (DownloadTimeManger.mode){
                            case TIME_PER_DAY:
                                Calendar tinstance=Calendar.getInstance();
                                tinstance.set(Calendar.HOUR_OF_DAY,hour);
                                tinstance.set(Calendar.MINUTE,minute);
                                if(tinstance.getTimeInMillis()-calendar.getTimeInMillis()<tolerance&&
                                        tinstance.getTimeInMillis()-calendar.getTimeInMillis()>=-1*tolerance){
                                    downloadAndUpServiceimpl.doJob();
                                }
                                break;
                            case TIME_PER_WEEK:
                                break;
                            default:
                                ;
                        }
                    }
                }
            };
        Timer timer = new Timer();
        String str=dates[0];
        int DAY=Integer.valueOf(str.substring(0,2));
        int hour=Integer.valueOf(str.substring(2,4));
        int minute=Integer.valueOf(str.substring(4,6));
        Calendar tcalendar=Calendar.getInstance();
        tcalendar.set(Calendar.HOUR_OF_DAY,hour);
        tcalendar.set(Calendar.MINUTE,minute);
        timer.scheduleAtFixedRate(timerTask,tcalendar.getTime(),PERIOD_HOUR);
    }


}

