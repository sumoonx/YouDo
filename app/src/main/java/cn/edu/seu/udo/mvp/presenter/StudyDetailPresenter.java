package cn.edu.seu.udo.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.seu.udo.cache.UserDBHelper;
import cn.edu.seu.udo.entities.AppUsage;
import cn.edu.seu.udo.entities.StudyTime;
import cn.edu.seu.udo.entities.UserRecord;
import cn.edu.seu.udo.mvp.view.StudyDetailIView;
import cn.edu.seu.udo.utils.UserUtil;

/**
 * Author: Jeremy Xu on 2016/4/10 13:53
 * E-mail: jeremy_xm@163.com
 */

public class StudyDetailPresenter extends Presenter<StudyDetailIView> {

    private ArrayList<UserRecord> userRecords;
    private List<StudyTime> studyTimes;

    public StudyDetailPresenter() {
    }

    @Override
    public void takeView(StudyDetailIView studyDetailView) {

        super.takeView(studyDetailView);

        userRecords = getUseRecords(5);
        studyTimes = generateStudyTimes();
    }

    public void getRank(String day) {
        StudyTime model = getStudyTime(day);
        iView.renderRank(model.getRank());
    }

    public void getStudyTimes() {
        iView.renderStudyTimes(studyTimes);
        iView.renderRank(studyTimes.get(studyTimes.size() - 1).getRank());
        iView.renderAppUsages(studyTimes.get(studyTimes.size() - 1).getAppUsageBrief());
    }

    public void getAppUsage(String day) {
        List<AppUsage> usageModels = getStudyTime(day).getAppUsageBrief();
        iView.renderAppUsages(usageModels);
    }

    private StudyTime getStudyTime(String day) {
        for (StudyTime model :
                studyTimes) {
            if (model.getDay().equals(day)) {
                return model;
            }
        }
        return null;
    }

    private List<StudyTime> generateStudyTimes() {
        List<StudyTime> studyTimes = new ArrayList<>();
        for(int i=userRecords.size()-1;i>=0;i--){
            UserRecord record = userRecords.get(i);
            studyTimes.add(new StudyTime(record.getRecordTimeString(),(int) record.getRank(),record));
        }
        return studyTimes;
    }
//
//    private List<AppUsage> generateAppUsages() {
//        List<AppUsage> usageModels = new ArrayList<>();
//        usageModels.add(new AppUsage("微信", getRandomHour()));
//        usageModels.add(new AppUsage("微博", getRandomHour()));
//        usageModels.add(new AppUsage("知乎", getRandomHour()));
//        usageModels.add(new AppUsage("有道", getRandomHour()));
//        usageModels.add(new AppUsage("邮件", getRandomHour()));
//        usageModels.add(new AppUsage("优酷", getRandomHour()));
//        usageModels.add(new AppUsage("土豆", getRandomHour()));
//        return usageModels;
//    }

    private float getRandomHour() {
        return (float) Math.random() * 2;
    }

    private int getRandomRank() {
        return (int)(Math.random() * 2000);
    }

    private ArrayList<UserRecord> getUseRecords(int num) {

        ArrayList<UserRecord> records = null;
        if (UserUtil.hasAccount()) {
            records = UserDBHelper.INSTANCE.query(UserUtil.getUserId(), num);
        } else {
            records = UserDBHelper.INSTANCE.query(0, num);
        }
        return records;
    }
}
