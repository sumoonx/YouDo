package cn.edu.seu.udo.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.UdoApplication;
import cn.edu.seu.udo.entities.AppInfo;

public class AppInfoUtil {

    @TargetApi(21)
    public static boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) UdoApplication.getUdoApplication()
                .getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats =
                usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isNoOption() {
        PackageManager packageManager =
                UdoApplication.getUdoApplication().getApplicationContext().getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 获取前台应用包名
     *
     * @return String
     */
    @SuppressWarnings("deprecation")
    public static String getAppPkgNameByTop() {

        if (Build.VERSION.SDK_INT >= 21 && isNoSwitch()) {
            UsageStatsManager usageStatsManager =
                    (UsageStatsManager) UdoApplication.getUdoApplication().getSystemService(Context.USAGE_STATS_SERVICE);
            long ts = System.currentTimeMillis();
            List<UsageStats> queryUsageStats = usageStatsManager
                    .queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 36000000, ts);
            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                return null;
            }
            UsageStats recentStats = null;
            for (UsageStats usageStats : queryUsageStats) {
                if (recentStats == null
                        || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed())
                    recentStats = usageStats;
            }
            return recentStats.getPackageName();
        } else {
            Context context =  UdoApplication.getUdoApplication();
            ActivityManager am =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
            return packageName;
        }
    }

    /**
     * 通过包名获取应用信息
     *
     * @return AppInfo
     */
    public static AppInfo getAppInfoByPkgName(String packageName) {
        Context context =  UdoApplication.getUdoApplication();
        PackageManager pm = context.getPackageManager();
        ApplicationInfo info = null;
        AppInfo appInfo = new AppInfo();
        if (packageName.equals("lock"))
            return appInfo;
        else if (packageName.equals("call")) {
            appInfo.setPackageName(packageName);
            appInfo.setLabel("通话");
            appInfo.setIcon(UdoApplication.getUdoApplication().getResources().getDrawable(R.drawable.lock));
            return appInfo;
        } else if (packageName.equals("其他")) {
            appInfo.setPackageName(packageName);
            appInfo.setLabel("其他");
            appInfo.setIcon(UdoApplication.getUdoApplication().getResources().getDrawable(R.drawable.lock));
            return appInfo;
        }
        try {
            info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            appInfo.setPackageName(packageName);
            appInfo.setLabel((String) info.loadLabel(pm));
            appInfo.setIcon(info.loadIcon(pm));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    /**
     * 通过包名获取应用名
     *
     * @return String
     */
    public static String getAppLableByPkgName(String packageName) {
        if (packageName.equals("lock"))
            return "锁屏";
        else if (packageName.equals("call")) return "通话";
        if (packageName.equals("其他")) return "其他";
        Context context =  UdoApplication.getUdoApplication();
        PackageManager pm = context.getPackageManager();
        ApplicationInfo info = null;
        String lable = "";
        try {
            info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            lable = (String) info.loadLabel(pm);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return lable;
    }

    /*
     * 获取桌面包名
     */
    public static String getLauncherPackageName() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = UdoApplication.getUdoApplication().getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            // 有多个桌面程序存在，且未指定默认项时；
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }

    /*
     * 获取桌面应用名
     */
    public static String getLauncherLabel() {
        return getAppLableByPkgName(getLauncherPackageName());
    }
    /*
     * 获取通话程序包名
     */
    public static String getPhonePackageName() {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + 0));
        final ResolveInfo res = UdoApplication.getUdoApplication().getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }



    /**
     * 查询手机内应用
     *
     * @param context
     * @return List<PackgeInfo>
     */
	public static List<PackageInfo> getAllApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
            // 判断是否为非系统预装的应用程序
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				// customs applications
				apps.add(pak);
			}
		}

		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
            // 判断是否为非系统预装的应用程序
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// customs applications
				apps.add(pak);
			}
		}
		return apps;
	}
}
