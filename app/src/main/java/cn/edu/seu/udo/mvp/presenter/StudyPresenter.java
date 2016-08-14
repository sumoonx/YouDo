package cn.edu.seu.udo.mvp.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;

import java.util.Locale;

import javax.inject.Inject;

import cn.edu.seu.udo.UdoApplication;
import cn.edu.seu.udo.mvp.view.StudyIView;
import cn.edu.seu.udo.service.CountTimeIntentService;
import cn.edu.seu.udo.ui.MainActivity;
import cn.edu.seu.udo.utils.AppInfoUtil;
import cn.edu.seu.udo.utils.ToastUtil;

/**
 * Author: Jeremy Xu on 2016/4/17 18:25
 * E-mail: jeremy_xm@163.com
 */
public class StudyPresenter extends Presenter<StudyIView> {

    private CountTimeIntentService countService = null;
    // 同service交互
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(iView == null)
                        return false;
                    int state = countService.getState();
                    switch (state) {
                        case CountTimeIntentService.STATE_COMPUTING:
                        case CountTimeIntentService.STATE_STORING:
                        case CountTimeIntentService.STATE_UPLOADING:
                            break;
                        case CountTimeIntentService.STATE_COUNTING:
                            iView.setTime(getTimeStr());
                            break;
                        case CountTimeIntentService.STATE_IDLE:
                            iView.setIdle();
                            break;
                        case CountTimeIntentService.STATE_RESULT:
                            iView.setResult(countService.getResult(),getTimeStr());
                            break;
                        default:
                    }
                    break;
                case 1:
                    ToastUtil.show(UdoApplication.getUdoApplication(), "您正在打开与学习无关的APP");
                    break;
                case 2:
                    ToastUtil.show(UdoApplication.getUdoApplication(), "已经使用"
                            + (countService.getLastAppTime() / 300000) * 5 + "分钟了");
                    break;
                default:
            }
            return true;
        }
    });

    @Inject
    public StudyPresenter() {
        syncWithCountTimeService();
    }

    public void showDetail() {
        iView.showDetail();
    }

    public void clickBtn(){
        if (countService.isRunning()) {
            countService.stop();
            iView.setComputing(getTimeStr());
        } else if(countService.getState() == CountTimeIntentService.STATE_RESULT) {
            countService.setState(CountTimeIntentService.STATE_IDLE);
            iView.setIdle();
        }else{
            if (Build.VERSION.SDK_INT >= 21) {
                if (AppInfoUtil.isNoOption() && !AppInfoUtil.isNoSwitch()) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    UdoApplication.getUdoApplication().startActivity(intent);
                    countService.setHasAlert(true);
                    ToastUtil.show(UdoApplication.getUdoApplication(), "请开启好大学应用权限");
                }
            }

            Intent service = new Intent(MainActivity.INSTANCE , CountTimeIntentService.class);
            MainActivity.INSTANCE.startService(service);

            iView.setCounting(getTimeStr());
        }
    }

    public void syncWithCountTimeService(){
        Intent service = new Intent(MainActivity.INSTANCE, CountTimeIntentService.class);

        MainActivity.INSTANCE.bindService(service, new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName arg0) {

            }

            @Override
            public void onServiceConnected(ComponentName arg0, IBinder arg1) {
                countService = ((CountTimeIntentService.MsgBinder) arg1).getService();
                countService.setHandler(myHandler);
                int state = countService.getState();
                switch (state) {
                    case CountTimeIntentService.STATE_COMPUTING:
                        iView.setComputing(getTimeStr());
                        break;
                    case CountTimeIntentService.STATE_COUNTING:
                        iView.setCounting(getTimeStr());
                        break;
                    case CountTimeIntentService.STATE_IDLE:
                        iView.setIdle();
                        SharedPreferences mySharedPreferences =
                                UdoApplication.getUdoApplication()
                                        .getSharedPreferences("service", Activity.MODE_PRIVATE);
                        String s = mySharedPreferences.getString("state", "");
                        if (s.equals("begin")) {
                            ToastUtil.show(UdoApplication.getUdoApplication(), "检测到程序被强行终止，请将自习菌添加到手机管家白名单中~");
                            SharedPreferences.Editor editor = mySharedPreferences.edit();
                            editor.putString("state", "stop");
                            editor.commit();
                        }
                        break;
                }
            }
        }, Context.BIND_AUTO_CREATE);
    }

    private String getTimeStr(){
        String time =
                String.format(Locale.SIMPLIFIED_CHINESE, "%02d:%02d:%02d",
                        countService.getHour(), countService.getMinute(),
                        countService.getSecond());
        return time;
    }
}
