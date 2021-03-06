package cn.edu.seu.udo.entities;

import android.support.annotation.ColorRes;

import cn.edu.seu.udo.R;

/**
 * Author: Jeremy Xu on 2016/6/21 17:42
 * E-mail: jeremy_xm@163.com
 */
public class Greeting {

    @ColorRes
    public static final int[] colors = {R.color.greeting_card_color1,
            R.color.greeting_card_color2, R.color.greeting_card_color3,
            R.color.greeting_card_color4, R.color.greeting_card_color5,
            R.color.greeting_card_color6, R.color.greeting_card_color7,
            R.color.greeting_card_color8,};

    private long id;
    private int thumbnail;
    private String nickName;
    private String content;
    private Time time;

    private static final Greeting empty = new Greeting(0, 0, "", "", new Time(0, 0));

    public Greeting(long id, int thumbnail, String nickName, String content, Time time) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.nickName = nickName;
        this.content = content;
        this.time = time;
    }

    public static Greeting getEmpty() {
        return empty;
    }

    public boolean isEmpty() {
        return id == 0;
    }

    public long getId() {
        return id;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public String getNickName() {
        return nickName;
    }

    public String getContent() {
        return content;
    }

    public Time getTime() {
        return time;
    }

    public String getTimeStr() {
        return time.toString();
    }

    public String getStruggle() {
        return "总共挣扎了100万次，";
    }

    public String getSuccessRate() {
        return "终于达成了90%的起床成功率！";
    }

    @ColorRes
    public int getColor() {
        if (isEmpty()) return R.color.greeting_card_backgroud;
        return colors[(int)Math.floor(Math.random() * 7) + 1];
    }
}
