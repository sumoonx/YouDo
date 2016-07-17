package cn.edu.seu.udo.mvp.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.seu.udo.mvp.view.RiseDetailIView;

/**
 * Author: Jeremy Xu on 2016/7/14 21:50
 * E-mail: jeremy_xm@163.com
 */
public class RiseDetailPresenter extends Presenter<RiseDetailIView> {

    public void getDates() {
        List<Date> dates = new ArrayList<>();
        Date date = new Date();
        dates.add(date);
        iView.renderRiseTime(date);
        date = new Date(date.getTime() - 500000000);
        dates.add(date);
        date = new Date(date.getTime() - 500000000);
        dates.add(date);
        date = new Date(date.getTime() - 500000000);
        dates.add(date);
        date = new Date(date.getTime() - 500000000);
        dates.add(date);
        iView.renderRiseDates(dates);
    }
}
