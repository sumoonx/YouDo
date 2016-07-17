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
import cn.edu.seu.udo.utils.AppInfoUtil;

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
            appName = intent.getStringExtra(ZXJAccessibilityService.PACKAGE_NAME);
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
        this.startForeground(1, MyNotification.getNotification());
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
            second = (int) (currentTime % 60);
            currentTime /= 60;
            minute = (int) (currentTime % 60);
            hour = (int) (currentTime /= 60);
            handler.sendEmptyMessage(0);

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

            CharSequence contentTitle = "正在自习中...";;
            CharSequence contentText =
                    "已经自习" + hour + "时" + minute + "分" + second + "秒   " + "当前应用:"
                            + AppInfoUtil.getAppLableByPkgName(currentApp);

            MyNotification.setsetLatestEventInfo(contentTitle,contentText);
            this.startForeground(1, MyNotification.getNotification());

            //handler.sendEmptyMessage(0);

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

    public void uploadAndStore(CountResult result) {

//        if (UserUtil.hasAccount()) {
//            int upload = 0;
//            String id = UserUtil.getAccount().getUsrId();
//            LogUtil.i("upload", id);
//            int _id = Integer.valueOf(id).intValue();
//            try {
//                String url = "http://api.learningjun.site/upload";
//                // 创建连接
//                HttpClient httpClient = new DefaultHttpClient();
//                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
//                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
//                HttpPost post = new HttpPost(url);
//                // 设置参数，仿html表单提交
//                ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
//                BasicNameValuePair pid = new BasicNameValuePair("user_id", id);
//
//                BasicNameValuePair pscore = new BasicNameValuePair("score", result.getScore() + "");
//                BasicNameValuePair pduration =
//                        new BasicNameValuePair("duration", result.getTotalTime() / 1000f + "");
//                BasicNameValuePair pdetail = new BasicNameValuePair("detail", result.getDetail());
//                BasicNameValuePair ptimestamp =
//                        new BasicNameValuePair("timestamp", result.getRecordTime() / 1000 + "");
//                paramList.add(pid);
//                paramList.add(pscore);
//                paramList.add(pduration);
//                paramList.add(pdetail);
//                paramList.add(ptimestamp);
//
//                post.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
//
//                // 发�?�HttpPost请求，并返回HttpResponse对象
//                HttpResponse httpResponse = httpClient.execute(post);
//                // Thread.sleep(500);
//                // LogUtil.i("upload", EntityUtils.toString(httpResponse
//                // .getEntity()));
//                // 判断请求响应状�?�码，状态码�?200表示服务端成功响应了客户端的请求
//                if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                    // 获取返回结果
//                    String response = EntityUtils.toString(httpResponse.getEntity());
//                    JSONObject json = new JSONObject(response);
//                    LogUtil.i("upload", "json:" + (float) json.getDouble("rank"));
//                    result.setRank((float) json.getDouble("rank"));
//                    upload = 1;
//                }
//            } catch (Exception e) {
//                upload = 0;
//                Toast.makeText(MyApp.getContext(), "网络错误，上传失�?", Toast.LENGTH_SHORT).show();
//                // LogUtil.e("upload", e.getMessage());
//            }
//            // 添加到本地数据库
//            UserDBHelper.INSTANCE.add(_id, result.getRecordTime(), result, result.getRank(),
//                    result.getScore(), upload);
//        } else {
//            // 添加到本地数据库
            UserDBHelper.INSTANCE.add(0, result.getRecordTime(), result, result.getRank(),
                    result.getScore(), 0);
//        }
    }
}
