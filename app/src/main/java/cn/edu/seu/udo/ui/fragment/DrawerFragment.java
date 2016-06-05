package cn.edu.seu.udo.ui.fragment;

import android.content.Context;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.utils.ToastUtil;

/**
 * Author: Jeremy Xu on 2016/6/2 14:02
 * E-mail: jeremy_xm@163.com
 */
public class DrawerFragment extends BaseFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_drawer;
    }

    @OnClick(R.id.profile_image)
    protected void startProfile() {
        ToastUtil.show(getActivity(), "Profile");
    }

    @OnClick(R.id.my_zoe)
    protected void startMyZoe() {
        ToastUtil.show(getActivity(), "My Zoe");
    }

    @OnClick(R.id.coupon)
    protected void startCoupon() {
        ToastUtil.show(getActivity(), "Coupon");
    }

    @OnClick(R.id.notice)
    protected void startNotice() {
        ToastUtil.show(getActivity(), "Notice");
    }

    @OnClick(R.id.logout)
    protected void startLogout() {
        ToastUtil.show(getActivity(), "Logout");
    }
}
