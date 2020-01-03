package com.hxd.gobus.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by pengyu520 on 2016/7/4.
 */
public class DateUtils {

    /**
     * 默认指定日期格式 yyyyMMddHHmmss
     */
    public static final String FORMAT_0 = "yyyyMMddHHmmss";
    /**
     * 指定日期格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 指定日期格式 yyyy-MM-dd HH:mm
     */
    public static final String FORMAT_2 = "yyyy-MM-dd HH:mm";
    /**
     * 指定日期格式 yyyy-MM-dd
     */
    public static final String FORMAT_3 = "yyyy-MM-dd";
    /**
     * 指定日期格式 HH:mm:ss
     */

    public static final String FORMAT_4 ="HH:mm:ss";
    /**
     * 指定日期格式HH:mm
     */
    public static final String FORMAT_5 = "HH:mm";
    /**
     * 指定日期格式 yyyy年MM月dd日
     */
    public static final String FORMAT_6= "yyyy年MM月dd日";
    /**
     * 指定日期格式 yyyy年MM月
     */
    public static final String FORMAT_8= "yyyy年MM月";
    /**
     * 指定日期格式 yyyy年MM月dd日 HH时mm秒
     */
    public static final String FORMAT_7= "yyyy年MM月dd日 HH时mm分";
    /**
     * 指定日期格式mm
     */
    public static final String FORMAT_9 = ":mm";

    /**
     * 指定日期格式 yyyy-MM-dd HH:mm
     */
    public static final String FORMAT_10 = "yyyy/MM/dd HH:mm";

    /**
     * 指定日期格式 yyyy-MM-dd HH:mm
     */
    public static final String FORMAT_11 = "MM/dd HH:mm";

    /**
     * 根据指定格式，获取当前的时间
     * @param format
     * @return
     */
    public static String getCurTimeFormat(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 把String日期转成指定格式的字符串日期
     * @param strDate
     * @param format
     * @return
     */
    public static String getStrDateFormat(String strDate, String format) {
        if (!TextUtils.isEmpty(strDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date;
            try {
                date = getFormat(FORMAT_1).parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return strDate;
            }
            return sdf.format(date);
        }
        return strDate;
    }


    public static SimpleDateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    /**
     * 将日期转化为指定格式的日期字符串
     * @param date
     * @param format
     * @return
     */
    public static String strByDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    /**
     * 获取当前的年月日 及星期
     * @return
     */
    public static String getYMDW(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mYear + "年" + mMonth + "月" + mDay+"日"+" 星期"+mWay;
    }

    /**
     * 判断在不在6个月之内 在 返回true
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isWhthinSixMonths(String startDate, String endDate){
        int fromY = Integer.parseInt(startDate.substring(0,4));
        int fromM;
        if(startDate.substring(5,7).charAt(0)== '0'){
            fromM = Integer.parseInt(startDate.substring(6,7));
        }else{
            fromM = Integer.parseInt(startDate.substring(5,7));
        }
        int fromD;
        if(startDate.substring(8,10).charAt(0)== '0'){
            fromD = Integer.parseInt(startDate.substring(9,10));
        }else{
            fromD = Integer.parseInt(startDate.substring(8,10));
        }

        int toY = Integer.parseInt(endDate.substring(0,4));
        int toM;
        if(endDate.substring(5,7).charAt(0)== '0'){
            toM = Integer.parseInt(endDate.substring(6,7));
        }else{
            toM = Integer.parseInt(endDate.substring(5,7));
        }
        int toD;
        if(endDate.substring(8,10).charAt(0)== '0'){
            toD = Integer.parseInt(endDate.substring(9,10));
        }else{
            toD = Integer.parseInt(endDate.substring(8,10));
        }

        if (toY - fromY > 1) {
            return false;
        }
        if (toY - fromY == 1) {
            int monthNum = 12  + toM - fromM;
            if (monthNum > 6) {
                return false;
            }
            if (monthNum == 6) {
                if (toD > fromD) {
                    return false;
                }
            }
        }
        if (fromY == toY) {
            if (toM - fromM > 6) {
                return false;
            }
            if (toM - fromM == 6) {
                if (toD > fromD) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断一个日期date在不在一定日期范围内
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isDateInOverly(String date, String startDate, String endDate){
        int year = Integer.parseInt(date.substring(0,4));
        int month;
        if(date.substring(5,7).charAt(0)== '0'){
            month = Integer.parseInt(date.substring(6,7));
        }else{
            month = Integer.parseInt(date.substring(5,7));
        }
        int day;
        if(date.substring(8,10).charAt(0)== '0'){
            day = Integer.parseInt(date.substring(9,10));
        }else{
            day = Integer.parseInt(date.substring(8,10));
        }

        int fromY = Integer.parseInt(startDate.substring(0,4));
        int fromM;
        if(startDate.substring(5,7).charAt(0)== '0'){
            fromM = Integer.parseInt(startDate.substring(6,7));
        }else{
            fromM = Integer.parseInt(startDate.substring(5,7));
        }
        int fromD;
        if(startDate.substring(8,10).charAt(0)== '0'){
            fromD = Integer.parseInt(startDate.substring(9,10));
        }else{
            fromD = Integer.parseInt(startDate.substring(8,10));
        }

        int toY = Integer.parseInt(endDate.substring(0,4));
        int toM;
        if(endDate.substring(5,7).charAt(0)== '0'){
            toM = Integer.parseInt(endDate.substring(6,7));
        }else{
            toM = Integer.parseInt(endDate.substring(5,7));
        }
        int toD;
        if(endDate.substring(8,10).charAt(0)== '0'){
            toD = Integer.parseInt(endDate.substring(9,10));
        }else{
            toD = Integer.parseInt(endDate.substring(8,10));
        }

        if(year == fromY && year == toY){
            //同年同月
            if(month == fromM && month == toM){
                if(day >= fromD && day <= toD){
                    return true;
                }
            }
            //同年不同月
            if(month == fromM && month < toM){
                if(day >= fromD){
                    return true;
                }
            }
            //同年不同月
            if(month > fromM && month == toM){
                if(day <= toD){
                    return true;
                }
            }
            //同年不同月
            if(month > fromM && month < toM){
                return true;
            }
        }else if(year == fromY && year < toY){
            if(month > fromM){
                return true;
            }else if(month == fromM){
                if(day > fromD){
                    return true;
                }
            }
        }else if(year > fromY && year == toY){
            if(month < toM){
                return true;
            }else if(month == toM){
                if(day < toD){
                    return true;
                }
            }
        }else if(year > fromY && year < toY){
            return true;
        }
        return false;
    }

    public static int[] StringToIntArr(String date){
        String[] strings = date.split("-");
        int[] arr = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            arr[i] = Integer.parseInt(strings[i]);
        }
        return arr;
    }

    public static String getCurrentDate(){
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_3);
        Date date = new Date();
        String curr = format.format(date);
        return curr;
    }

    /**
     * 和当前日期比较      返回值：大于当前日期：1，等于当前日期：0，小于当前日期：-1
     * @param date
     * @return
     */
    public static int equalsDate(String date , String currentDate){
        //传进来的日期数组
        int[] dataArr = StringToIntArr(date);
        //当前日期数组
        int[] current = StringToIntArr(currentDate);
        //进行比较
        if (dataArr[0]>current[0]) {
            return 1;
        }
        if (dataArr[0]==current[0]) {
            //年份相等，判断月份
            if (dataArr[1]>current[1]) {
                return 1;
            }else if(dataArr[1]==current[1]){
                //月份相等，判断天
                if (dataArr[2]>current[2]) {
                    return 1;
                }else if(dataArr[2]==current[2]){
                    return 0;
                }
                return -1;
            }
        }
        return -1;
    }

    /**
     * 将日期转化为星期
     * @param date
     * @return
     * @throws Exception
     */
    public static String getWeek(String date){
        String weekEnd = null;
        int week = 0;
        try {
            week = dateTransferWeek(date);
            switch (week){
                case 1:
                    weekEnd = "星期一";
                    break;
                case 2:
                    weekEnd = "星期二";
                    break;
                case 3:
                    weekEnd = "星期三";
                    break;
                case 4:
                    weekEnd = "星期四";
                    break;
                case 5:
                    weekEnd = "星期五";
                    break;
                case 6:
                    weekEnd = "星期六";
                    break;
                case 7:
                    weekEnd = "星期日";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weekEnd;
    }

    public static int dateTransferWeek(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_3);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        int week = 0;
        if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
            week = 7;
        }else{
            week = calendar.get(Calendar.DAY_OF_WEEK)-1;
        }
        return week;
    }

    /**
     * 将年月日替换为-
     * @param date
     * @return
     */
    public static String transferDateTime(String date){
        String newDate = date.replace("年","-");
        newDate = newDate.replace("月","-");
        newDate = newDate.replace("日","");
        return newDate;
    }

    /**
     * 将年月替换为-
     * @param date
     * @return
     */
    public static String transferDateTime2(String date){
        String newDate = date.replace("年","-");
        newDate = newDate.replace("月","-");
        return newDate;
    }

    /**
     * 比较两个时间大小
     * @param nowTime
     * @param startTime
     * @return
     */
    public static boolean compareTime(String nowTime, String startTime){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_2);
            Date a = sdf.parse(nowTime);
            Date b = sdf.parse(startTime);
            //Date类的一个方法，如果a早于b返回true，否则返回false
            if(a.before(b)){
                return true;
            }else{
                return false;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否超过24个小时
     * @param startTimeMillis
     * @param timeLength
     * @return
     */
    public static boolean isMoreThan24(long startTimeMillis,long timeLength) {
        if((startTimeMillis + timeLength) > 24 * 60 * 60 * 1000){
            return true;
        }
        return false;
    }

    /**
     * 通过毫秒数获取时间
     * @param time
     * @return
     * @throws ParseException
     */
    public static String getTimeForMillis(long time){
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_5);
        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hm = format.format(time);
        return hm;
    }

    /**
     * 转换关系
     * @param time
     * @return
     */
    public static String getTextTime(String time){
        String textTime = null;
        if(time.equals("00:30")){
            textTime = "半小时";
        }else if(time.equals("01:00")){
            textTime = "一小时";
        }else if(time.equals("01:30")){
            textTime = "一个半小时";
        }else if(time.equals("02:00")){
            textTime = "两小时";
        }else if(time.equals("02:30")){
            textTime = "两个半小时";
        }else if(time.equals("03:00")){
            textTime = "三小时";
        }else if(time.equals("03:30")){
            textTime = "三个半小时";
        }else if(time.equals("04:00")){
            textTime = "四小时";
        }else if(time.equals("04:30")){
            textTime = "四个半小时";
        }else if(time.equals("05:00")){
            textTime = "五小时";
        }else if(time.equals("05:30")){
            textTime = "五个半小时";
        }else if(time.equals("06:00")){
            textTime = "六小时";
        }else if(time.equals("06:30")){
            textTime = "六个半小时";
        }else if(time.equals("07:00")){
            textTime = "七小时";
        }else if(time.equals("07:30")){
            textTime = "七个半小时";
        }else if(time.equals("08:00")){
            textTime = "八小时";
        }else if(time.equals("08:30")){
            textTime = "八个半小时";
        }else if(time.equals("09:00")){
            textTime = "九小时";
        }else if(time.equals("09:30")){
            textTime = "九个半小时";
        }else if(time.equals("10:00")){
            textTime = "十小时";
        }else if(time.equals("10:30")){
            textTime = "十个半小时";
        }else if(time.equals("11:00")){
            textTime = "十一个小时";
        }else if(time.equals("11:30")){
            textTime = "十一个半小时";
        }else if(time.equals("12:00")){
            textTime = "十二个小时";
        }else if(time.equals("12:30")){
            textTime = "十二个半小时";
        }else if(time.equals("13:00")){
            textTime = "十三个小时";
        }else if(time.equals("13:30")){
            textTime = "十三个半小时";
        }else if(time.equals("14:00")){
            textTime = "十四个小时";
        }else if(time.equals("14:30")){
            textTime = "十四个半小时";
        }else if(time.equals("15:00")){
            textTime = "十五个小时";
        }else if(time.equals("15:30")){
            textTime = "十五个半小时";
        }else if(time.equals("16:00")){
            textTime = "十六个小时";
        }else if(time.equals("16:30")){
            textTime = "十六个半小时";
        }else if(time.equals("17:00")){
            textTime = "十七个小时";
        }else if(time.equals("17:30")){
            textTime = "十七个半小时";
        }else if(time.equals("18:00")){
            textTime = "十八个小时";
        }
        return textTime;
    }

    /**
     * 判断时间是否在时间段内
     * @param beginStr
     * @param endStr
     * @return  0表示在时间段之内  -1 表示早于  1表示大于
     */
    public static String belongCalendar(String beginStr, String endStr) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_1);
        try {
            Date nowDate = df.parse(df.format(new Date()));
            Date beginDate = df.parse(beginStr);
            Date endDate = df.parse(endStr);
            Calendar now = Calendar.getInstance();
            now.setTime(nowDate);
            Calendar begin = Calendar.getInstance();
            begin.setTime(beginDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            if (now.after(begin) && now.before(end)) {
                return "0";
            } else if(now.before(begin) && now.before(end)){
                return "-1";
            }else if(now.after(begin) && now.after(end)){
                return "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断时间是否在时间范围内
     * @param beginStr
     * @param endStr
     * @return  0表示在时间段之内  -1 表示早于  1表示大于
     */
    public static String isInTimeRange(String beginStr, String endStr) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_4);
        try {
            Date nowDate = df.parse(df.format(new Date()));
            Date beginDate = df.parse(beginStr);
            Date endDate = df.parse(endStr);
            Calendar now = Calendar.getInstance();
            now.setTime(nowDate);
            Calendar begin = Calendar.getInstance();
            begin.setTime(beginDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            if (now.after(begin) && now.before(end)) {
                return "0";
            } else if(now.before(begin) && now.before(end)){
                return "-1";
            }else if(now.after(begin) && now.after(end)){
                return "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
