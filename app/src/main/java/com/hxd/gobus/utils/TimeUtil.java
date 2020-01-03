package com.hxd.gobus.utils;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by wangqingbin on 2018/4/9.
 */

public class TimeUtil {

    /**
     * 判断时间1是否早于时间2  false
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isEarly(String time1,String time2){
        DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try{
            c1.setTime(df.parse(time1));
            c2.setTime(df.parse(time2));
        }catch(java.text.ParseException e){
            e.printStackTrace();
        }
        int result = c1.compareTo(c2);
        if(result <= 0){
            return false;
        } else{
            return true;
        }
    }

    /**
     * 比较当前时间是否大于规定的下班时间  大于 true
     */
    public static boolean equalsSettingEndTime(String clockInTime,String endSettingTime){
        String[] temp = clockInTime.split(":");
        String[] xiaban = endSettingTime.split(":");
        if(Integer.parseInt(temp[0]) > Integer.parseInt(xiaban[0])){
            return true;
        }else if(Integer.parseInt(temp[0]) == Integer.parseInt(xiaban[0])){
            if(Integer.parseInt(temp[1]) > Integer.parseInt(xiaban[1])){
                return true;
            }else if(Integer.parseInt(temp[1]) == Integer.parseInt(xiaban[1])){
                if(Integer.parseInt(temp[2]) > Integer.parseInt(xiaban[2])){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 比较当前时间是否大于规定的下班时间  大于 true
     */
    public static boolean equalsSettingTimeForGroup(String clockInTime,String endSettingTime){
        String[] temp = clockInTime.split(":");
        String[] xiaban = endSettingTime.split(":");
        if(Integer.parseInt(temp[0]) > Integer.parseInt(xiaban[0])){
            return true;
        }else if(Integer.parseInt(temp[0]) == Integer.parseInt(xiaban[0])){
            if(Integer.parseInt(temp[1]) >= Integer.parseInt(xiaban[1])){
                return true;
            }
        }
        return false;
    }

    /**
     * 比较当前时间是否大于规定的下班时间  大于 1 等于 0 小于 -1
     */
    public static int equalsSettingEndTime3(String clockInTime,String endSettingTime){
        String[] strings = clockInTime.split(":");
        String[] xiabanTimes = endSettingTime.split(":");
        int[] temp = new int[strings.length];
        int[] xiaban =new int[xiabanTimes.length];
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        for (int i = 0; i < xiabanTimes.length; i++) {
            xiaban[i]=Integer.parseInt(xiabanTimes[i]);
        }
        if (temp[0]>xiaban[0]) {
            return 1;
        }
        if(temp[0]==xiaban[0]){
            if (temp[1]>xiaban[1]) {
                return 1;
            }
            if (temp[1]==xiaban[1]) {   //如果分钟也相等的话 说明马上要大于了，因为规定的下班时间的秒都是00
                return 0;
            }
        }
        return -1;
    }

    /**
     * 小于1分钟返回true
     * @param clockInTime
     * @param endSettingTime
     * @return
     */
    public static boolean equalsSettingEndTime2(String clockInTime,String endSettingTime){
        String[] strings = clockInTime.split(":");
        String[] xiabanTimes = endSettingTime.split(":");
        int[] temp = new int[strings.length];
        int[] xiaban =new int[xiabanTimes.length];
        for (int i = 0; i < strings.length; i++) {
            temp[i]=Integer.parseInt(strings[i]);
        }
        for (int i = 0; i < xiabanTimes.length; i++) {
            xiaban[i]=Integer.parseInt(xiabanTimes[i]);
        }
        if(temp[0]==xiaban[0]){
            //如果分钟相等 那么1定是在1分钟之内
            if(temp[1]==xiaban[1]){
                return true;
            }
            //如果分钟恰好相差1分钟
            if(Math.abs(temp[1]-xiaban[1])==1){
                if(temp[1] > xiaban[1]){
                    if(temp[2] <= xiaban[2]){
                        return true;
                    }
                }else if(temp[1] < xiaban[1]){
                    if(xiaban[2] <= temp[2]){
                        return true;
                    }
                }
            }
        }else if(temp[1] == 59 && (xiaban[0]-temp[0]) == 1){
            if(xiaban[1] == 0){
                return true;
            }
        }else if(xiaban[1] == 59 && (temp[0]-xiaban[0])==1){
            if(temp[1] == 0){
                return true;
            }
        }
        return false;
    }

}
