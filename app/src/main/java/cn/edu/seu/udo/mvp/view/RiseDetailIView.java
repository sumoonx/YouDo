package cn.edu.seu.udo.mvp.view;

import java.util.Date;
import java.util.List;

/**
 * Author: Jeremy Xu on 2016/7/14 21:50
 * E-mail: jeremy_xm@163.com
 */
public interface RiseDetailIView extends IView {
    void renderRiseTime(Date date);

    void renderRiseDates(List<Date> dates);
}
