package com.zjy.js.customdialog.activity;

/**
 Created by 张建宇 on 2018/11/28. */
public class MySingleton {
    public static class SFactory {
        private static MySingleton mSingle = new MySingleton();
    }

    private static final MySingleton ourInstance = new MySingleton();

    private MySingleton() {

    }

    public static MySingleton getInstance() {
        return SFactory.mSingle;
    }
}
