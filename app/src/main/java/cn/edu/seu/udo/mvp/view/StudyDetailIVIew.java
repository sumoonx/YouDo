package cn.edu.seu.udo.mvp.view;

import java.util.List;

import cn.edu.seu.udo.model.entities.AppUsage;
import cn.edu.seu.udo.model.entities.StudyTime;

/**
 * Author: Jeremy Xu on 2016/6/5 21:55
 * E-mail: jeremy_xm@163.com
 */
public interface StudyDetailIView extends IView {
    void renderRank(int rank);

    void renderStudyTimes(List<StudyTime> studyTimes);

    void renderAppUsages(List<AppUsage> appUsages);
}
