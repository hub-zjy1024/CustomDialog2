//
// Created by js on 2019/9/9.
//

#ifndef CUSTOMDIALOG_ANDROIDUTIL_H
#define CUSTOMDIALOG_ANDROIDUTIL_H

#endif //CUSTOMDIALOG_ANDROIDUTIL_H
#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/bitmap.h>
#include <opencv2/imgproc/imgproc.hpp>
using namespace std;
using namespace cv;
class AndroidOpencvUtil {
public:
    void BitmapToMat2(JNIEnv *env, jobject& bitmap, Mat& mat, jboolean needUnPremultiplyAlpha) ;
    void BitmapToMat(JNIEnv *env, jobject& bitmap, Mat& mat) ;
    void MatToBitmap2
            (JNIEnv *env, Mat& mat, jobject& bitmap, jboolean needPremultiplyAlpha) ;
    void MatToBitmap(JNIEnv *env, Mat& mat, jobject& bitmap);
};
