package com.zjy.js.customdialog.opencvutils;


import android.util.Log;

/**
 Created by 张建宇 on 2018/8/15. */
public class ImageUtils {
    static {
        try {
            System.loadLibrary("opencv_zjy");
        } catch (Exception e) {
            Log.e("zjy", "load zjyopencv failed", e);
        } catch (UnsatisfiedLinkError e) {
            Log.e("zjy", "load zjyopencv failed", e);
        }
    }

    public static native int[] getModifyOrientation(int[] pixs, int width, int height);

}
