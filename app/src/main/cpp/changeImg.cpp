//
// Created by js on 2019/9/6.
//

#include <jni.h>
#include <iostream>
#include <cmath>
#include <fstream>
#include <memory>
#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>
#include <opencv2/core/types.hpp>
#include <opencv2/core/types_c.h>
#include <opencv2/core/core_c.h>

using namespace std;
using namespace cv;
Mat imgDataToMat(JNIEnv *env, jintArray buf, jint w, jint h) {

    jint *cbuf;
    cbuf = env->GetIntArrayElements(buf, 0);
    Mat myimg(h, w, CV_8UC4, (unsigned char *) cbuf);


    Mat srcImage = imread("F:\\opencv_re_learn\\2.jpg");
    cvtColor(srcImage,cv2.COLOR_BGR2GRAY)
    if (!srcImage.data){
        cout << "falied to read" << endl;
        system("pause");
        return;
    }
    Mat srcGray = cvtColor(srcImage, srcGray, CV_BGR2GRAY);

    //高斯滤波
    GaussianBlur(srcGray, srcGray, Size(3, 3),
                 0, 0);
    //Canny检测
    int edgeThresh =100;
    Mat Canny_result;
    Canny(srcImage, Canny_result, edgeThresh, edgeThresh * 3, 3);
    return myimg;
}
//img = cv2.imread('style.jpg')
//gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
//edges = cv2.Canny(gray, 50, 150, apertureSize=3)
//
//# 霍夫变换
//lines = cv2.HoughLines(edges, 1, np.pi / 180, 0)
//rotate_angle = 0
//for rho, theta in lines[0]:
//a = np.cos(theta)
//b = np.sin(theta)
//x0 = a * rho
//y0 = b * rho
//x1 = int(x0 + 1000 * (-b))
//y1 = int(y0 + 1000 * (a))
//x2 = int(x0 - 1000 * (-b))
//y2 = int(y0 - 1000 * (a))
//if x1 == x2 or y1 == y2:
//continue
//t = float(y2 - y1) / (x2 - x1)
//rotate_angle = math.degrees(math.atan(t))
//if rotate_angle > 45:
//rotate_angle = -90 + rotate_angle
//elif rotate_angle < -45:
//rotate_angle = 90 + rotate_angle
//print("rotate_angle : "+str(rotate_angle))
//rotate_img = ndimage.rotate(img,rotate_angle)
//imageio.imwrite('result.png',rotate_img)
//cv2.imshow("img",rotate_img)