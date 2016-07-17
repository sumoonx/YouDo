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
     * ��ȡǰ̨Ӧ�ð���
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
     * ͨ��������ȡӦ����Ϣ
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
            appInfo.setLabel("ͨ��");
            appInfo.setIcon(UdoApplication.getUdoApplication().getResources().getDrawable(R.drawable.lock));
            return appInfo;
        } else if (packageName.equals("����")) {
            appInfo.setPackageName(packageName);
            appInfo.setLabel("����");
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
     * ͨ��������ȡӦ����
     *
     * @return String
     */
    public static String getAppLableByPkgName(String packageName) {
        if (packageName.equals("lock"))
            return "����";
        else if (packageName.equals("call")) return "ͨ��";
        if (packageName.equals("����")) return "����";
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
     * ��ȡ�������
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
            // �ж�����������ڣ���δָ��Ĭ����ʱ��
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }

    /*
     * ��ȡ����Ӧ����
     */
    public static String getLauncherLabel() {
        return getAppLableByPkgName(getLauncherPackageName());
    }
    /*
     * ��ȡͨ���������
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
     * ��ѯ�ֻ���Ӧ��
     *
     * @param context
     * @return List<PackgeInfo>
     */
	public static List<PackageInfo> getAllApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
        // ��ȡ�ֻ�������Ӧ��
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
            // �ж��Ƿ�Ϊ��ϵͳԤװ��Ӧ�ó���
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				// customs applications
				apps.add(pak);
			}
		}

		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
            // �ж��Ƿ�Ϊ��ϵͳԤװ��Ӧ�ó���
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// customs applications
				apps.add(pak);
			}
		}
		return apps;
	}
}
