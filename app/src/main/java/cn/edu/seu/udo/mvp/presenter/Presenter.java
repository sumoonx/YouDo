/*
 * Copyright (c) 2016. Team Udo from Southeast University
 * All rights reserved.
 */

package cn.edu.seu.udo.mvp.presenter;

import cn.edu.seu.udo.mvp.view.IView;

/**
 * Author: Jeremy Xu on 2016/4/5 20:54
 * E-mail: jeremy_xm@163.com
 */
public abstract class Presenter<V extends IView> {

    protected V iView;

    public void takeView(V v) {
        iView = v;
    }

    public void dropView() {
        iView = null;
    }

}
