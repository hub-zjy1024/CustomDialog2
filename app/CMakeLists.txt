# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)
#配置加载native依赖

#set(OpenCv_Home ${CMAKE_SOURCE_DIR}/../sdk/native)
#include_directories(src/main/jni)
set(OpenCv_Home ${CMAKE_SOURCE_DIR}/src/main/jniLibs)

set(cppDir ${CMAKE_SOURCE_DIR}/src/main/cpp)

include_directories(${cppDir})
#引入libopencv_java3.so文件
add_library(lib_opencv SHARED IMPORTED )
#set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${OpenCv_Home}/${ANDROID_ABI}/libopencv_java3.so)

add_library( # Specifies the name of the library.
             jniTest
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
        ${cppDir}/main.cpp)
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
find_library( # Sets the name of the path variable.
              bitmap
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              jnigraphics )
add_library(opencv_zjy SHARED ${cppDir}/opencvImgUtils.cpp ${cppDir}/fuliyeTest.cpp ${cppDir}/changeImg.cpp ${cppDir}/androidUtil.cpp)

target_link_libraries(opencv_zjy ${log-lib} lib_opencv ${bitmap})

target_link_libraries( # Specifies the target library.
                       jniTest ${log-lib} )


