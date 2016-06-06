package cn.edu.seu.udo.mvp.presenter;

import javax.inject.Inject;

import cn.edu.seu.udo.mvp.view.StudyIView;

/**
 * Author: Jeremy Xu on 2016/4/17 18:25
 * E-mail: jeremy_xm@163.com
 */
public class StudyPresenter implements Presenter<StudyIView> {

    private StudyIView studyView;

    @Inject
    public StudyPresenter() {}

    @Override
    public void takeView(StudyIView studyView) {
        this.studyView = studyView;
    }

    @Override
    public void dropView() {
        studyView = null;
    }

    public void showDetail() {
        studyView.showDetail();
    }
}
