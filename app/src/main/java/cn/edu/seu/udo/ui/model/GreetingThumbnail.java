package cn.edu.seu.udo.ui.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;

/**
 * Author: Jeremy Xu on 2016/6/22 15:46
 * E-mail: jeremy_xm@163.com
 */
public class GreetingThumbnail extends CardThumbnail {

    public GreetingThumbnail(Context context) {
        super(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View imageView) {
        ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder()
                .setupView(imageView);
        getParentCard().setViewToClickToExpand(viewToClickToExpand);
    }
}
