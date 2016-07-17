
/**
 * @description:
 * 
 *               ZxjAccessibilityService listens on windowStateChanged events and obtains both
 *               application name and event time.
 * 
 *               We broadcast messages using an intent, you need to register a BroadcastReceiver.
 * 
 *               The configuration file is at /res/xml/zxj_accessibility_service_config.xml.
 * 
 *               Remember to register this service in the manifest file, permission
 *               android.permission.BIND_ACCESSIBILITY_SERVICE is needed.
 * 
 *               To enable accessibility service, you can add this code to your source file: {
 *               Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
 *               startActivity(intent); }
 * 
 * @author: JeremyXu via jeremy_xm@163.com
 * 
 * @time: 15-12-19 17:34
 * 
 * @version: v0.0
 */
package cn.edu.seu.udo.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class ZXJAccessibilityService extends AccessibilityService {
    private final static String TAG = "ZXJAccessibilityService";

    public final static String INTENT_NAME = "com.zxj.current_application.accessibility_service";
    public final static String PACKAGE_NAME = "package_name";
    public final static String EVENT_TIME = "event_time";

    private Intent broadcastIntent = new Intent(INTENT_NAME);

    public ZXJAccessibilityService() {
        Log.d(TAG, "Constructed...");
    }

    /**

     * Filtered accessibility event will trigger this call-back function.
     * Through which, We obtain application name and event time.
     *
     * @param accessibilityEvent filtered accessibility event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String packageName = accessibilityEvent.getPackageName().toString();
        long event_time = accessibilityEvent.getEventTime();
        broadcastIntent.putExtra(PACKAGE_NAME, packageName);
        broadcastIntent.putExtra(EVENT_TIME, event_time);
        sendBroadcast(broadcastIntent);
        Log.d(TAG, packageName + " at time: " + event_time);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected...");
        //setServiceInfo();
    }

    @Override
    public void onInterrupt() {
        //do nothing
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy...");
    }

    /**
     * dynamically configure accessibility service
     */
    private void setServiceInfo() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        // We are interested in all types of accessibility events.
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        // We want to provide specific type of feedback.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        // We want to receive events in a certain interval.
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        
        final String service = "com.seu.zxj.service/com.seu.zxj.service.ZXJAccessibilityService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    Log.v(TAG, "-------------- > accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }
}
