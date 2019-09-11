//
// Created by js on 2019/9/9.
//

#ifndef CUSTOMDIALOG_ANDROIDLOG_H
#define CUSTOMDIALOG_ANDROIDLOG_H

#endif //CUSTOMDIALOG_ANDROIDLOG_H

#include <jni.h>
#include <android/log.h>
#define TAG "android_opencv"
#define dir "/sdcard/opencv"

#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, ##__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, ##__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG,__VA_ARGS__)




