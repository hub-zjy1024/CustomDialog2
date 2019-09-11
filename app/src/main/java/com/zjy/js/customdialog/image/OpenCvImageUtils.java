package com.zjy.js.customdialog.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 Created by 张建宇 on 2018/8/15. */
public class OpenCvImageUtils {
    static {
        System.loadLibrary("opencv_java3");
    }

    /**
     测试透视变换
     */
    public Bitmap testWarpPerspective(String filePath) {
        Bitmap bit = BitmapFactory.decodeFile(filePath);
        return testWarpPerspective(bit);
    }

    /**
     测试透视变换
     */
    public Bitmap testWarpPerspective2(String filePath) {
        Mat src = HandleImgUtils.matFactory(filePath);
        Mat dest = HandleImgUtils.warpPerspective(src);
        Bitmap bit = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dest, bit);
        return bit;
    }

    /**
     测试透视变换
     */
    public Bitmap testWarpPerspective(Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Mat target = HandleImgUtils.warpPerspective(mat);
        Bitmap newBm = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        mat.release();
        Utils.matToBitmap(target, newBm, true);
        target.release();
        return newBm;
    }

    public void testWarpPerspectiveJava(String path, String destPath) {
        Mat src = HandleImgUtils.matFactory(path);
        src = HandleImgUtils.warpPerspective(src);
        HandleImgUtils.saveImg(src, destPath);

    }

    /**
     测试透视变换
     */
    public void testWarpPerspective() {
        Mat src = HandleImgUtils.matFactory("C:/Users/admin/Desktop/opencv/open/q/x10.jpg");
        src = HandleImgUtils.warpPerspective(src);
        HandleImgUtils.saveImg(src, HandleImgUtils.opencv_work_dir2 + "x10-testWarpPerspective.jpg");
    }
}
