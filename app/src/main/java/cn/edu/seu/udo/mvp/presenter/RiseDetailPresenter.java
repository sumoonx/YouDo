package cn.edu.seu.udo.mvp.presenter;

import cn.edu.seu.udo.mvp.view.RiseDetailIView;

/**
 * Author: Jeremy Xu on 2016/7/14 21:50
 * E-mail: jeremy_xm@163.com
 */
public class RiseDetailPresenter implements Presenter<RiseDetailIView> {

    RiseDetailIView view;

    @Override
    public void takeView(RiseDetailIView riseDetailIView) {
        view = riseDetailIView;
    }

    @Override
    public void dropView() {
        view = null;
    }
}
