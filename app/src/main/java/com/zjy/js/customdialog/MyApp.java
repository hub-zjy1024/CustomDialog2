package com.zjy.js.customdialog;

import android.app.Application;

import org.xutils.x;

/**
 Created by 张建宇 on 2018/11/28. */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
