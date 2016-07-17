package cn.edu.seu.udo;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.edu.seu.udo.entities.AppsItemInfo;
import cn.edu.seu.udo.utils.AppInfoUtil;

/**
 * Author: Jeremy Xu on 2016/5/30 13:49
 * E-mail: jeremy_xm@163.com
 */
public class UdoApplication extends Application {

    private static UdoApplication udoApplication;

    public static HashSet<String> whiteSet = new HashSet<String>(); // 应用白名单
    public static ArrayList<AppsItemInfo> whiteApps = new ArrayList<AppsItemInfo>();
    public static ArrayList<AppsItemInfo> blackApps = new ArrayList<AppsItemInfo>();
    @Override
    public void onCreate() {
        super.onCreate();
        udoApplication = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences mySharedPreferences = udoApplication
                        .getSharedPreferences("white", Activity.MODE_PRIVATE);
                String s = mySharedPreferences.getString("apps", "");
                String apps[] = s.split(";");
                for (int i = 0; i < apps.length; ++i) {
                    whiteSet.add(apps[i]);
                }
                whiteSet.add("cn.edu.seu.udo");
                whiteSet.add("lock");
                whiteSet.add("call");
                whiteSet.add(AppInfoUtil.getLauncherPackageName());
                whiteSet.remove("");
                PackageManager pManager = udoApplication.getPackageManager();
                List<PackageInfo> appList = AppInfoUtil.getAllApps(udoApplication);
                for (int i = 0; i < appList.size(); i++) {
                    PackageInfo pinfo = appList.get(i);
                    AppsItemInfo shareItem = new AppsItemInfo();
                    shareItem.setIcon(pManager
                            .getApplicationIcon(pinfo.applicationInfo));
                    shareItem.setLabel(pManager.getApplicationLabel(
                            pinfo.applicationInfo).toString());
                    shareItem.setPackageName(pinfo.applicationInfo.packageName);

                    if (shareItem.getPackageName().equals("com.seu.zxj")
                            || shareItem.getPackageName().equals(
                            AppInfoUtil.getLauncherPackageName()))
                        continue;
                    if (whiteSet.contains(shareItem.getPackageName())) {
                        shareItem.setSelected(true);
                        whiteApps.add(shareItem);
                    } else {
                        shareItem.setSelected(false);
                        blackApps.add(shareItem);
                    }
                }
            }
        }).start();

        initializeIconify();
    }

    public static UdoApplication getUdoApplication() {
        return udoApplication;
    }

    private void initializeIconify() {
        Iconify.with(new FontAwesomeModule());
    }
}
