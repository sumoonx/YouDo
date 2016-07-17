package cn.edu.seu.udo.mvp.presenter;

import javax.inject.Inject;

import cn.edu.seu.udo.mvp.view.StudyIView;

/**
 * Author: Jeremy Xu on 2016/4/17 18:25
 * E-mail: jeremy_xm@163.com
 */
public class StudyPresenter extends Presenter<StudyIView> {

    @Inject
    public StudyPresenter() {}

    public void showDetail() {
        iView.showDetail();
    }
}
