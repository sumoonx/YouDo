package cn.edu.seu.udo.model.entities;

/**
 * Author: Jeremy Xu on 2016/6/21 17:42
 * E-mail: jeremy_xm@163.com
 */
public class Greeting {
    private long id;
    private int thumbnail;
    private String nickName;
    private String content;
    private Time time;

    public Greeting(long id, int thumbnail, String nickName, String content, Time time) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.nickName = nickName;
        this.content = content;
        this.time = time;
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

    public boolean isLast() {
        return id == 0;
    }
}
