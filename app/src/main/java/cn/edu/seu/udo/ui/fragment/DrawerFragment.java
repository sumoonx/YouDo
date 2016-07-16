package cn.edu.seu.udo.ui.fragment;

import butterknife.OnClick;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.utils.ToastUtil;

/**
 * Author: Jeremy Xu on 2016/6/2 14:02
 * E-mail: jeremy_xm@163.com
 */
public class DrawerFragment extends InteractFragment {

    public static final String TAG = "drawer";

    @Override
    protected int getLayout() {
        return R.layout.fragment_drawer;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @OnClick(R.id.profile_image)
    protected void startProfile() {
        activityInteraction.doInteract(HomeFragment.START);
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
