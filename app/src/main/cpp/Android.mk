LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

NDK_PROJECT_PATH=$(APP_PROJECT_PATH)
$(warning NDK_PROJECT_PATH=$(NDK_PROJECT_PATH))
$(warning LOCAL_PATH=$(LOCAL_PATH))
opencv_home:=D:/downloads/CustomDialog/sdk
ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
# include指向自己OpenCV-android-sdk/sdk/native/jni/OpenCV.mk对应位置
else
include $(OPENCV_MK_PATH)
endif

LOCAL_MODULE := jniTest
LOCAL_SRC_FILES := main.cpp
LOCAL_LDLIBS += -lm -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_3_4_2
LOCAL_SRC_FILES := ../jniLibs/${TARGET_ARCH_ABI}/libopencv_java3.so
$(warning srcFile=$(LOCAL_SRC_FILES))

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
OPENCV_INSTALL_MODULES:=on
LOCAL_MODULE := opencv_zjy
#LOCAL_C_INCLUDES +=$(LOCAL_PATH)
LOCAL_SRC_FILES := opencvImgUtils.cpp fuliyeTest.cpp changeImg.cpp androidUtil.cpp
LOCAL_SHARED_LIBRARIES :=opencv_3_4_2
LOCAL_LDLIBS += -lm -llog -ljnigraphics

include $(BUILD_SHARED_LIBRARY)
