package cn.edu.seu.udo.mvp.view;

import cn.edu.seu.udo.bean.WeatherBean;

/*
 *desc 
 *author rhg
 *time 2016/6/6 13:49
 *email 1013773046@qq.com
 */
public interface WeatherIView<T> extends IView{
    public void show(T t);
}
