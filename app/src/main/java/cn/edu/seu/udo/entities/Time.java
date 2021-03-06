package cn.edu.seu.udo.entities;

import java.util.Date;

/**
 * Author: Jeremy Xu on 2016/6/22 21:58
 * E-mail: jeremy_xm@163.com
 */
public class Time {
    private int hour;
    private int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getTime(char seprater) {
        return "" + hour + seprater + minute;
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }
}
