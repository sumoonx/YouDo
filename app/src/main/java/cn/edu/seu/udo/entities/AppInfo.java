package cn.edu.seu.udo.entities;

import cn.edu.seu.udo.R;
import android.graphics.drawable.Drawable;

import cn.edu.seu.udo.UdoApplication;

public class AppInfo {
    private String label;
    private String packageName;
    private Drawable icon;

    public AppInfo() {
        label = "����";
        packageName = "lock";
        icon = UdoApplication.getUdoApplication().getResources().getDrawable(R.drawable.lock);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean equals(AppInfo app) {
        return this.packageName.equals(app.packageName);
    }

}
