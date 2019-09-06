APP_PLATFORM = android-18
#APP_ABI := armeabi-v7a x86 arm64-v8a
APP_ABI :=all
#APP_STL := stlport_static
#已经过时
#APP_STL := gnustl_static
APP_STL := c++_static
APP_CPPFLAGS += -std=c++11
APP_CPPFLAGS += -fexceptions
APP_CPPFLAGS += -frtti
APP_OPTIM := debug