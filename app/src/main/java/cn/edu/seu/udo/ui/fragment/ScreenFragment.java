package cn.edu.seu.udo.ui.fragment;

import android.os.Bundle;

import cn.edu.seu.udo.utils.LogUtil;

/**
 * Author: Jeremy Xu on 2016/7/15 21:15
 * E-mail: jeremy_xm@163.com
 */
public abstract class ScreenFragment extends InteractFragment {

    public static final String START = "start_";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        LogUtil.i("Fragment " + getName() + "is added to Screen");
    }

    @Override
    public void onDestroy() {
        LogUtil.i("Fragment " + getName() + "is removed from Screen");
        super.onDestroy();
    }

    protected void initInjector() {

    }

    public abstract String getTitle();

    public abstract String getIntent();
}
