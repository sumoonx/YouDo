package cn.edu.seu.udo.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.mvp.presenter.StudyPresenter;
import cn.edu.seu.udo.mvp.view.StudyIView;
import cn.edu.seu.udo.service.CountResult;
import cn.edu.seu.udo.service.TimeAxisItem;
import cn.edu.seu.udo.utils.AppInfoUtil;
import cn.edu.seu.udo.utils.LogUtil;

/**
 * Author: Jeremy Xu on 2016/6/5 22:09
 * E-mail: jeremy_xm@163.com
 */
public class StudyFragment extends ScreenFragment implements StudyIView{

    public static final String TAG = "StudyFragment";

    public static final String START = ScreenFragment.START + TAG;

    @Inject StudyPresenter presenter;

    @BindView(R.id.btn_startLearn)  Button btn_learn;
    @BindView(R.id.txt_learnTime)  TextView txt_time;
    @BindView(R.id.txt_learnResult)  TextView txt_result;

    @Override
    public void onStart() {
        super.onStart();
        //initInjector();
        presenter.takeView(this);
        btn_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickBtn();
            }
        });
    }

    @Override
    public void onStop() {
        presenter.dropView();
        super.onStop();
    }

    @Override
    public String getTitle() {
        return "自习";
    }

    @Override
    public String getIntent() {
        return START;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_study;
    }

    @Override @OnClick(R.id.btn_learnDetail)
    public void showDetail() {
        LogUtil.i("show detail");
        activityInteraction.doInteract(StudyDetailFragment.START);
    }

    @Override
    public void setTime(String time) {
        txt_time.setText(time);
    }

    @Override
    public void setCounting(String time) {
        btn_learn.setText("   ||   ");
        txt_time.setText(time);
    }

    @Override
    public void setComputing(String time) {
        btn_learn.setText("分数计算");
        btn_learn.setClickable(false);
        txt_time.setText(time);
    }

    @Override
    public void setIdle() {
        btn_learn.setText("开始自习");
        btn_learn.setClickable(true);
        txt_time.setText("00:00:00");
        txt_result.setText("");
    }

    @Override
    public void setResult(CountResult result,String time) {
        btn_learn.setText("自习结束");
        btn_learn.setClickable(true);
        txt_time.setText(time);

        String detail = "";
        ArrayList<TimeAxisItem> list = result.getAppTimeList();
        for(int i=0;i<list.size();i++){
            detail += "("+AppInfoUtil.getAppLableByPkgName(list.get(i).getPkgName())+":"+list.get(i).getTimeSeconds()+"s)";
        }
        txt_result.setText(detail);
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        presenter =  new StudyPresenter();
    }

    //    private void initInjector() {
//        presenter =  new StudyPresenter();
//    }
}
