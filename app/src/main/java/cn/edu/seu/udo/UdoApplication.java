package cn.edu.seu.udo.udo;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Author: Jeremy Xu on 2016/5/30 13:49
 * E-mail: jeremy_xm@163.com
 */
public class UdoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initializeIconify();
    }

    private void initializeIconify() {
        Iconify.with(new FontAwesomeModule());
    }
}
