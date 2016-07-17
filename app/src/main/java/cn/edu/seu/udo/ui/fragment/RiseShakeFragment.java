package cn.edu.seu.udo.ui.fragment;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.mvp.view.RiseShakeIView;

/**
 * Author: Jeremy Xu on 2016/7/17 16:52
 * E-mail: jeremy_xm@163.com
 */
public class RiseShakeFragment extends ScreenFragment implements RiseShakeIView {

    public static String TAG = "RiseShakeFragment";

    public static String START = ScreenFragment.START + TAG;

    @Override
    public String getTitle() {
        return "早起";
    }

    @Override
    public String getIntent() {
        return START;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_rise_shake;
    }
}
