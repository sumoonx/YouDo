package cn.edu.seu.udo.entities;

import java.util.Date;

/**
 * Author: Jeremy Xu on 2016/7/17 11:10
 * E-mail: jeremy_xm@163.com
 */
public class RiseTime {
    private Date date;

    public RiseTime(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
