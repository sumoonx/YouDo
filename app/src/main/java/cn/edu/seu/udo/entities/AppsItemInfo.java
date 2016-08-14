package cn.edu.seu.udo.entities;

import android.graphics.drawable.Drawable;

public class AppsItemInfo {

    private Drawable icon; 
    private String label; 
    private String packageName; 
    private boolean selected = false;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
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
    
    public void setSelected(boolean b){
    	selected = b;
    }
    
    public boolean getSelected(){
    	return selected;
    }
}
