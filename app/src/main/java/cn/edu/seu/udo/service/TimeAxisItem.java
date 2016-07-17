package cn.edu.seu.udo.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TimeAxisItem implements Serializable {

    private static final long serialVersionUID = -3781314796205894015L;

    private String pkgName;
    private Long time;

    TimeAxisItem(String s) {
        pkgName = s;
        time = 1l;
    }

    TimeAxisItem(String s, long t) {
        pkgName = s;
        time = t;
    }

    public void addTime(long time) {
        this.time += time;
    }



    public static final String KEY_NAME = "appname";
    public static final String KEY_TIME = "usetime";
    public static final String KEY_INFO_STRING = "userinfostring";

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_NAME, pkgName);
            jsonObject.put(KEY_TIME, time);
        } catch (JSONException e) {}
        return jsonObject;
    }

    public String toJsonObjectString() {
        return pkgName + "," + time.toString() + ",";
    }

    public String getPkgName() {
        return pkgName;
    }

    public Long getTime() {
        return time;
    }

    public float getTimeSeconds() {
        return time / 1000f;
    }
}
