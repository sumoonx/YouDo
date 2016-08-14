package cn.edu.seu.udo.mvp.view;

import cn.edu.seu.udo.service.CountResult;

/**
 * Author: Jeremy Xu on 2016/6/5 21:55
 * E-mail: jeremy_xm@163.com
 */
public interface StudyIView extends IView{
    void showDetail();

    void setTime(String time);
    void setCounting(String time);
    void setComputing(String time);
    void setIdle();
    void setResult(CountResult result,String time);
}
