package cn.edu.seu.udo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author: Jeremy Xu on 2016/7/17 15:13
 * E-mail: jeremy_xm@163.com
 */
public class DateUtil {

    private static Calendar calendar = Calendar.getInstance();

    private static DateFormat monthFormat = new SimpleDateFormat("M", Locale.CHINA);
    private static DateFormat dayFormat = new SimpleDateFormat("d", Locale.CHINA);
    private static DateFormat hourFormat = new SimpleDateFormat("H", Locale.CHINA);
    private static DateFormat minuteFormat = new SimpleDateFormat("mm", Locale.CHINA);

    public static Date currentDate() {
        return new Date();
    }

    public static String getMonthStr(Date date) {
        return monthFormat.format(date);
    }

    public static String getDayStr(Date date) {
        return dayFormat.format(date);
    }

    public static String getHourStr(Date date) {
        return hourFormat.format(date);
    }

    public static String getMinuteStr(Date date) {
        return minuteFormat.format(date);
    }

    public static int getMonth(Date date) {
        return Integer.valueOf(getMonthStr(date));
    }

    public static int getDay(Date date) {
        return Integer.valueOf(getDayStr(date));
    }

    public static int getHour(Date date) {
        return Integer.valueOf(getHourStr(date));
    }

    public static int getMinute(Date date) {
        return Integer.valueOf(getMinuteStr(date));
    }

    public static String getDateStr(Date date) {
        return getMonthStr(date) + "/" + getDayStr(date);
    }

    public static String getTimeStr(Date date) {
        return getHourStr(date) + ":" + getMinuteStr(date);
    }

    public static float getDateFloat(Date date) {
        return getMonth(date) + getDay(date) / 31f;
    }

    public static float getTimeFloat(Date date) {
        return getHour(date) + getMinute(date) / 60f;
    }

    public static String dateStrFrom(float date) {
        int month = (int) date;
        int day = (int) (Math.round((date - month) * 31));
        return "" + month + "/" + day;
    }

    public static String timeStrFrom(float date) {
        int hour = (int) date;
        int minute = Math.round((date - hour) * 60);
        return "" + hour + ":" + minute;
    }

    public static Date nextDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, +1);
        return cal.getTime();
    }
}
