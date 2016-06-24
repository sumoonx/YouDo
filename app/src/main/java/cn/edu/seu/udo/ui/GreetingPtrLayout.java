package cn.edu.seu.udo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author: Jeremy Xu on 2016/6/23 15:26
 * E-mail: jeremy_xm@163.com
 */
public class GreetingPtrLayout extends PtrFrameLayout {

    public GreetingPtrLayout(Context context) {
        super(context, null);
    }

    public GreetingPtrLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public GreetingPtrLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
//        super.dispatchTouchEvent(e);
//        View view = getChildAt(0);
//        view.dispatchTouchEvent(e);
//        return true;
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
