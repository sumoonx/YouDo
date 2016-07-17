package cn.edu.seu.udo.entities;

import java.util.List;

/**
 * Author: Jeremy Xu on 2016/6/2 21:19
 * E-mail: jeremy_xm@163.com
 */
public class Tool {
    private int iconRes;
    private String name;
    private boolean onState = false;
    private List<String> infos;

    public Tool(int iconRes, String name, List<String> infos) {
        this.iconRes = iconRes;
        this.name = name;
        this.infos = infos;
    }

    public int getIconRes() {
        return iconRes;
    }

    public String getName() {
        return name;
    }

    public boolean isOnState() {
        return onState;
    }

    public List<String> getInfos() {
        return infos;
    }
}
