package com.zjy.js.customdialog;


public class JniTest {
    static {
        System.loadLibrary("jniTest");
    }

    public native String getString(String a, String b);

    public native String getGrayArray(int[] buf, int w, int h);
}
