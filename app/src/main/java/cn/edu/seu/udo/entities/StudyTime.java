package cn.edu.seu.udo.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.seu.udo.service.TimeAxisItem;
import cn.edu.seu.udo.utils.AppInfoUtil;
import cn.edu.seu.udo.utils.LogUtil;

/**
 * Author: Jeremy Xu on 2016/4/11 10:30
 * E-mail: jeremy_xm@163.com
 */
public class StudyTime implements Comparable<StudyTime> {

    public static final int BRIEF_SIZE = 6;

    private final String day;
    private int rank;
    private float totalHour;
    private List<AppUsage> appUsages;

    public StudyTime(String day, int rank, List<AppUsage> appUsages) {
        this.day = day;
        this.rank = rank;
        setAppUsages(appUsages);
    }

    public StudyTime(String day, int rank, UserRecord record) {
        this.day = day;
        this.rank = rank;
        LogUtil.i(record.getRecordTimeString() + "==>" + record.getData().getTotalTime()/1000f);
        this.totalHour =  record.getData().getTotalTime()/1000f;
        setAppUsages(record);
    }

    public StudyTime(String day, List<AppUsage> appUsages) {
        this(day, 0, appUsages);
    }

    public StudyTime(String day) {
        this(day, 0, new ArrayList<AppUsage>());
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setAppUsages(List<AppUsage> appUsages) {
        totalHour = 0;
        this.appUsages = appUsages;
        for (AppUsage model :
                appUsages) {
            totalHour += model.getHour();
        }
    }

    public void setAppUsages(UserRecord record) {
        appUsages = new ArrayList<AppUsage>();
        ArrayList<TimeAxisItem> appTimeList = record.getData().getAppTimeList();
        for(TimeAxisItem item : appTimeList){
            appUsages.add(new AppUsage(AppInfoUtil.getAppLableByPkgName(item.getPkgName()),(float)item.getTimeSeconds()));
        }
    }

    public void updateAppUsageModelByName(String appName, float hour) {
        AppUsage appUsage = findAppUsageModelByName(appName);
        if (appUsage != null) {
            totalHour += hour - appUsage.getHour();
            appUsage.setHour(hour);
        } else {
            addAppUsageModel(new AppUsage(appName, hour));
        }
    }

    public void updateAppUsageModel(AppUsage appUsage) {
        updateAppUsageModelByName(appUsage.getAppName(), appUsage.getHour());
    }

    public void addAppUsageModel(AppUsage appUsage) {
        if (appUsage != null) {
            appUsages.add(appUsage);
            totalHour += appUsage.getHour();
        }
    }

    public List<AppUsage> getAppUsageBrief() {
        List<AppUsage> models = new ArrayList<>();
        Collections.sort(appUsages);

        int i = 0;
        while (i < appUsages.size() && i < BRIEF_SIZE) {
            models.add(appUsages.get(i++));
        }
        if (i >= BRIEF_SIZE) {
            AppUsage other = new AppUsage("其他", models.get(BRIEF_SIZE - 1).getHour());
            while (i < appUsages.size()) {
                other.addHour(appUsages.get(i++).getHour());
            }
            models.remove(BRIEF_SIZE - 1);
            models.add(other);
        }

        return models;
    }

    public List<AppUsage> getAppUsageLeft() {
        List<AppUsage> models = new ArrayList<>();
        Collections.sort(appUsages);

        if (appUsages.size() > BRIEF_SIZE) {
            for (int i = BRIEF_SIZE; i < appUsages.size(); i++) {
                models.add(appUsages.get(i));
            }
        }

        return models;
    }

    public String getDay() {
        return day;
    }

    public int getRank() {
        return rank;
    }

    public float getTotalHour() {
        return totalHour;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** StudyTime *****\n");
        stringBuilder.append("Study time at " + day + " is: ");
        stringBuilder.append(totalHour + "hours\n");
        for (AppUsage model :
                appUsages) {
            stringBuilder.append(model + "\n");
        }
        stringBuilder.append("**************************\n");

        return stringBuilder.toString();
    }
    @Override
    public int compareTo(StudyTime another) {
        float hour = another.getTotalHour();
        return hour < totalHour ? -1 : (hour == totalHour ? 0 : 1);
    }

    private AppUsage findAppUsageModelByName(String target) {
        AppUsage result = null;
        for (AppUsage model :
                appUsages) {
            if (model.getAppName().equals(target)) {
                result = model;
                break;
            }
        }
        return result;
    }

    private AppUsage findAppUsageModel(AppUsage target) {
        String appName = target.getAppName();
        return findAppUsageModelByName(appName);
    }
}
