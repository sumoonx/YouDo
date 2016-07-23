package cn.edu.seu.udo.service;

import android.app.Activity;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.edu.seu.udo.UdoApplication;
import cn.edu.seu.udo.cache.UserDBHelper;
import cn.edu.seu.udo.entities.UploadResult;
import cn.edu.seu.udo.rest.ApiManager;
import cn.edu.seu.udo.utils.AppInfoUtil;
import cn.edu.seu.udo.utils.LogUtil;
import cn.edu.seu.udo.utils.UserUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CountTimeIntentService extends IntentService {

    public static final int STATE_COUNTING = 0;
    public static final int STATE_COMPUTING = 1;
    public static final int STATE_UPLOADING = 2;
    public static final int STATE_STORING = 3;
    public static final int STATE_IDLE = 4;
    public static final int STATE_RESULT = 5;

    private int state;
    private String appName="Udo";
    // private static final String TAG = "CountTimeIntentService";

    private static CountTimeIntentService instance;
    private long startTime;
    private long lastTime;
    private int hour;
    private int minute;
    private int second;
    private int millis;
    private int precision;
    private boolean isRun = false;
    private Handler handler;
    private ZXJAccServiceReceiver zxjAccServiceReceiver = null;
    private String crntAppAlert;
    private Set<String> appSet;
    private ArrayList<TimeAxisItem> timeAxis;
    private CountResult result;
    private String currentApp = "";
    private boolean reStart = false;
    private boolean doAlert = false;
    private boolean hasAlert = false;
    private boolean normalEnd = true;

    public CountTimeIntentService() {
        super("CountTimeIntentService");
        precision = 100;
        timeAxis = new ArrayList<TimeAxisItem>();
        instance = this;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public boolean isRunning() {
        return isRun;
    }

    public void stop() {
        isRun = false;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getMillis() {
        return millis;
    }

    public String getCurrentApp() {
        return currentApp;
    }

    @Override
    public void onCreate() {
        state = STATE_IDLE;
        super.onCreate();
    }

    public class ZXJAccServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            appName = intent.getStringExtra(ZXJAccessibilityService.PACKAGE_NAME);
            // eventTime = intent.getLongExtra("event_time", -1);
            // Log.d(TAG, appName + " at time: " + eventTime);
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {

//        zxjAccServiceReceiver = new ZXJAccServiceReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.zxj.current_application.accessibility_service");
//        registerReceiver(zxjAccServiceReceiver, intentFilter);

        SharedPreferences mySharedPreferences =
                getSharedPreferences("service", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("state", "begin");
        editor.commit();

        appSet = new HashSet<String>();

        normalEnd = true;
        startTime = System.currentTimeMillis();
        lastTime = 0;
        state = STATE_COUNTING;
     //   handler.sendEmptyMessage(0);
        isRun = true;
        currentApp = "";
        hour = 0;
        minute = 0;
        second = 0;
        millis = 0;
        super.onStart(intent, startId);
        this.startForeground(1, MyNotification.getINSTANCE().getNotification());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        while (isRun) {
            long start = 0;
            long end = 0;

            start = System.currentTimeMillis();
            // �ص�����
            if (reStart) {
                reStart = false;
                Intent intent1 = new Intent(Intent.ACTION_MAIN);
                intent1.addCategory(Intent.CATEGORY_HOME);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }

            long currentTime = System.currentTimeMillis() - startTime;
            // �������6Сʱ
            if (currentTime > 21600000) {
                isRun = false;
                normalEnd = false;
                break;
            }
            long duraTime = currentTime - lastTime;
            lastTime = currentTime;

            millis = (int) (currentTime % 1000);
            currentTime /= 1000;
            int second_tmp = (int) (currentTime % 60);
            if(second_tmp != second){
                second = second_tmp;
                CharSequence contentTitle = "正在自习中...";
                CharSequence contentText =
                        "已经自习" + hour + "时" + minute + "分" + second + "秒   " + "当前应用:"
                                + AppInfoUtil.getAppLableByPkgName(currentApp);
                MyNotification.setLatestEventInfo(contentTitle,contentText);
                handler.sendEmptyMessage(0);
            }
            currentTime /= 60;
            minute = (int) (currentTime % 60);
            hour = (int) (currentTime /= 60);

            String newApp = "";
            KeyguardManager mKeyguardManager =
                    (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
            boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
            if (flag) {
                newApp = "lock";
            } else {
                String s = AppInfoUtil.getAppPkgNameByTop();
 //               String s = appName;
                if (s != null)
                    newApp = s;
                else
                    newApp = currentApp;
            }

            if (isTelephonyCalling()) {
                newApp = "call";
            }

            if (currentApp.equals(newApp)) {
                timeAxis.get(timeAxis.size() - 1).addTime(duraTime);
            } else
                timeAxis.add(new TimeAxisItem(newApp, duraTime));

            if (!UdoApplication.whiteSet.contains(newApp) && !appSet.contains(newApp)) {
                if (!currentApp.equals(newApp) && !hasAlert) {
                    hasAlert = true;
                    crntAppAlert = newApp;
                    handler.sendEmptyMessage(1);
                } else if ((timeAxis.get(timeAxis.size() - 1).getTime() / 1000) % 300 == 0) {
                    if (!hasAlert && doAlert) {
                        hasAlert = true;
                        handler.sendEmptyMessage(2);
                    }
                }
            }
            currentApp = newApp;
            end = System.currentTimeMillis();
            try {
                if (end - start < precision) Thread.sleep(precision - end + start);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (normalEnd) {
            // 计算结果
            state = STATE_COMPUTING;
            result = new CountResult(timeAxis);

            uploadAndStore(result);
            // 传递给结果界面
            state = STATE_RESULT;
            handler.sendEmptyMessage(0);
        }
        timeAxis.clear();
        doAlert = false;
        //state = STATE_IDLE;
        //handler.sendEmptyMessage(0);
        this.stopForeground(true);

        SharedPreferences mySharedPreferences =
                getSharedPreferences("service", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("state", "stop");
        editor.commit();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        public CountTimeIntentService getService() {
            return CountTimeIntentService.this;
        }
    }

    public CountResult getResult(){
        return result;
    }

    public void setState(int i) {
        state = i;
    }

    public int getState() {
        return state;
    }

    public static CountTimeIntentService getInstance() {
        return instance;
    }

    public long getLastAppTime() {
        return timeAxis.get(timeAxis.size() - 1).getTime();
    }

    public void setAlert(boolean b) {
        doAlert = b;
    }

    public void setHasAlert(boolean b) {
        hasAlert = b;
    }

    public void addToAppSet() {
        appSet.add(crntAppAlert);
    }

    public void quitApp() {
        reStart = true;
    }

    private boolean isTelephonyCalling() {
        boolean calling = false;
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyManager.CALL_STATE_OFFHOOK == telephonyManager.getCallState()
                || TelephonyManager.CALL_STATE_RINGING == telephonyManager.getCallState()) {
            calling = true;
        }
        return calling;
    }

    private class ResultObserver implements Observer<UploadResult>{

        private CountResult countResult;

        ResultObserver(CountResult countResult){
            this.countResult = countResult;
        }
        @Override
        public void onCompleted() {
            LogUtil.i("completed");
        }
        @Override
        public void onError(Throwable e) {
            LogUtil.i(""+e.getMessage());
            UserDBHelper.INSTANCE.add(UserUtil.getUserId(), result.getRecordTime(), result, result.getRank(),
                    result.getScore(), 0);
        }
        @Override
        public void onNext(UploadResult result) {
            LogUtil.i(result.getRank()+","+result.getState());
            UserDBHelper.INSTANCE.add( UserUtil.getUserId(), countResult.getRecordTime(), countResult,(float) result.getRank(),
                    countResult.getScore(), 1);
        }
    }

    public void uploadAndStore(CountResult result) {

        if (UserUtil.hasAccount()) {
        ApiManager.getApiManager().getApiService().uploadRecord("0",result.getScore()+"",result.getTotalTime() / 1000f + "",result.getDetail(),result.getRecordTime()/ 1000+"")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ResultObserver(result));
        } else {
            // 添加到本地数据库
            UserDBHelper.INSTANCE.add(0, result.getRecordTime(), result, result.getRank(),
                    result.getScore(), 0);
        }
    }
}
