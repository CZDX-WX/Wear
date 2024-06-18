package com.czdxwx.wear;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        // 在这里进行应用程序级别的初始化操作
        Log.d(TAG, "Application started");

    }





}