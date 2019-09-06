# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)
#配置加载native依赖

#set(OpenCv_Home ${CMAKE_SOURCE_DIR}/../sdk/native)
#include_directories(src/main/jni)
set(OpenCv_Home ${CMAKE_SOURCE_DIR}/../sdk/native)
include_directories(${OpenCv_Home}/jni/include)
#引入libopencv_java3.so文件
add_library(lib_opencv SHARED IMPORTED )
#set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${OpenCv_Home}/libs/${ANDROID_ABI}/libopencv_java3.so)

add_library( # Specifies the name of the library.
             jniTest
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             src/main/cpp/main.cpp)
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
add_library(zjyopencv SHARED ./src/main/cpp/opencvImgUtils.cpp )
target_link_libraries(zjyopencv ${log-lib} lib_opencv)
target_link_libraries( # Specifies the target library.
                       jniTest ${log-lib} )


