package cn.edu.seu.udo.model.entities;

/**
 * Created by rhg on 2016/6/6.
 */
public class WeatherBean {
    public WeatherBean(String wendu, String data, String type) {
        this.wendu = wendu;
        this.data = data;
        this.type = type;
    }

    private String wendu;

    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public String getWendu() {
        return wendu;
    }

    private String data;
    private String type;

}