package cn.edu.seu.udo.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.UdoApplication;


public class MyNotification{
    private static NotificationManager mNotificationManager;
    private static MyNotification INSTANCE = new MyNotification();
    private static NotificationCompat.Builder builder;

    private MyNotification() {
        Context context =  UdoApplication.getUdoApplication();
        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("自习进行中...")
                .setContentText("")
                .setTicker("开始自习！")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher);

    }

    public static MyNotification getINSTANCE(){
        return INSTANCE;
    }

    public Notification getNotification() {
        Notification  notification =  builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        return notification;
    }

    public static void setLatestEventInfo(CharSequence contentTitle, CharSequence contentText) {
        builder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis());

        mNotificationManager.notify(1,builder.build());
    }
}
