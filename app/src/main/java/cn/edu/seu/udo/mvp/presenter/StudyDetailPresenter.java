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
        if(userRecords.size()!=0) {
            iView.renderStudyTimes(studyTimes);
            iView.renderRank(studyTimes.get(studyTimes.size() - 1).getRank());
            iView.renderAppUsages(studyTimes.get(studyTimes.size() - 1).getAppUsageBrief());
        }
    }

    public void getAppUsage(String day) {
        StudyTime studyTime = getStudyTime(day);
        if(studyTime!=null) {
            List<AppUsage> usageModels = studyTime.getAppUsageBrief();
            iView.renderAppUsages(usageModels);
        }
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
