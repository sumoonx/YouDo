package cn.edu.seu.udo.ui.fragment;

import javax.inject.Inject;

import butterknife.OnClick;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.mvp.presenter.StudyPresenter;
import cn.edu.seu.udo.mvp.view.StudyIView;
import cn.edu.seu.udo.utils.LogUtil;

/**
 * Author: Jeremy Xu on 2016/6/5 22:09
 * E-mail: jeremy_xm@163.com
 */
public class StudyFragment extends BaseFragment implements StudyIView{

    public static final String START = "start_study";

    @Inject StudyPresenter presenter;

    @Override
    public void onStart() {
        super.onStart();
        initInjector();
        presenter.takeView(this);
    }

    @Override
    public void onStop() {
        presenter.dropView();
        super.onStop();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_study;
    }

    @Override @OnClick(R.id.start_detail)
    public void showDetail() {
        LogUtil.i("show detail");
        fragmentInteractionListener.onFragmentInteraction(StudyDetailFragment.START);
    }

    private void initInjector() {
        presenter =  new StudyPresenter();
    }
}
