package cn.edu.seu.udo.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.edu.seu.udo.model.AppUsage;
import cn.edu.seu.udo.model.StudyTime;
import cn.edu.seu.udo.mvp.view.StudyDetailIView;
import cn.edu.seu.udo.utils.LogUtil;

/**
 * Author: Jeremy Xu on 2016/4/10 13:53
 * E-mail: jeremy_xm@163.com
 */
public class StudyDetailPresenter implements Presenter<StudyDetailIView> {

    private StudyDetailIView studyDetailView;

    private List<StudyTime> studyTimes;

    @Inject
    public StudyDetailPresenter() {
    }

    @Override
    public void takeView(StudyDetailIView studyDetailView) {
        this.studyDetailView = studyDetailView;
        generateAppUsages();
    }

    @Override
    public void dropView() {
        studyDetailView = null;
    }

    public void getRank(String day) {
        StudyTime model = getStudyTime(day);
        studyDetailView.renderRank(model.getRank());
    }

    public void getStudyTimes() {
        studyTimes = generateStudyTimes();
        LogUtil.ai(studyTimes.toString());
        studyDetailView.renderStudyTimes(studyTimes);
        studyDetailView.renderRank(studyTimes.get(studyTimes.size() - 1).getRank());
        studyDetailView.renderAppUsages(studyTimes.get(studyTimes.size() - 1).getAppUsageBrief());
    }

    public void getAppUsage(String day) {
        List<AppUsage> usageModels = getStudyTime(day).getAppUsageBrief();
        studyDetailView.renderAppUsages(usageModels);
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
        studyTimes.add(new StudyTime("周三", getRandomRank(), generateAppUsages()));
        studyTimes.add(new StudyTime("周四", getRandomRank(), generateAppUsages()));
        studyTimes.add(new StudyTime("周五", getRandomRank(), generateAppUsages()));
        studyTimes.add(new StudyTime("周六", getRandomRank(), generateAppUsages()));
        studyTimes.add(new StudyTime("周日", getRandomRank(), generateAppUsages()));
        studyTimes.add(new StudyTime("周一", getRandomRank(), generateAppUsages()));
        studyTimes.add(new StudyTime("周二", 0, generateAppUsages()));
        return studyTimes;
    }

    private List<AppUsage> generateAppUsages() {
        List<AppUsage> usageModels = new ArrayList<>();
        usageModels.add(new AppUsage("微信", getRandomHour()));
        usageModels.add(new AppUsage("微博", getRandomHour()));
        usageModels.add(new AppUsage("知乎", getRandomHour()));
        usageModels.add(new AppUsage("有道", getRandomHour()));
        usageModels.add(new AppUsage("邮件", getRandomHour()));
        usageModels.add(new AppUsage("优酷", getRandomHour()));
        usageModels.add(new AppUsage("土豆", getRandomHour()));
        return usageModels;
    }

    private float getRandomHour() {
        return (float) Math.random() * 2;
    }

    private int getRandomRank() {
        return (int)(Math.random() * 2000);
    }
}
