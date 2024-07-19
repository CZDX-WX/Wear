package com.czdxwx.wear;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.hjq.toast.Toaster;
import com.tencent.mmkv.MMKV;

import cn.jiguang.api.utils.JCollectionAuth;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        // 在这里进行应用程序级别的初始化操作
        Log.d("应用已启动", "Application started");
        JPushInterface.setDebugMode(true);

        // 调整点一：初始化代码前增加setAuth调用
        boolean isPrivacyReady; // app根据是否已弹窗获取隐私授权来赋值

        JPushInterface.init(this);


        // 调整点二：App用户同意了隐私政策授权，并且开发者确定要开启推送服务后调用
        JCollectionAuth.setAuth(this, true); //如初始化被拦截过，将重试初始化过程

        Toaster.init(this);
        MMKV.initialize(this);


    }

    private void initChannel() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup("MyGroupId", "自定义通知组");
            nm.createNotificationChannelGroup(notificationChannelGroup);

            NotificationChannel notificationChannel = new NotificationChannel("m_channel123", "自定义通知 1", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setGroup("MyGroupId");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(Uri.parse("android.resource://com.czdxwx.wear/raw/music"), null);

            nm.createNotificationChannel(notificationChannel);
        }
    }

}