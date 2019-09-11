//
// Created by js on 2018/8/15.
//

/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_zjy_js_customdialog_JniTest */
#include <iostream>
#include <cmath>
#include <fstream>
#include <memory>
#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>
#include <opencv2/core/types.hpp>
#include <opencv2/core/types_c.h>
#include <opencv2/core/core_c.h>
#include "AndroidLog.h"
using namespace std;
using namespace cv;
//IplImage* change4channelTo3InIplImage(IplImage*);
//
//void onMouse(int, int , int , int, void*);


#ifndef _Included_com_zjy_js_customdialog_JniTest
#define _Included_com_zjy_js_customdialog_JniTest

int throwException(JNIEnv *env, const char *msg) {
    jclass exCla = env->FindClass("com/zjy/js/customdialog/opencvutils/ExTest");
    env->ThrowNew(exCla, msg);
    return JNI_ERR;
}
#ifdef __cplusplus
extern "C" {
#endif
Point2f srcTri[4], dstTri[4];
int clickTimes = 0;  //在图像上单击次数
Mat image;
Mat imageWarp;
IplImage* change4channelTo3InIplImage(IplImage * src) {
//    if (src->nChannels != 4) {
//        return NULL;
//    }
//
//    IplImage *destImg = cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 3);
//    for (int row = 0; row < src->height; row++) {
//        for (int col = 0; col < src->width; col++) {
//            CvScalar s = cvGet2D(src, row, col);
//            cvSet2D(destImg, row, col, s);
//        }
//    }

//    return destImg;
    return  src;
}
typedef int32_t BYTE;
BYTE* dealWith(BYTE *pCur, int x, int y) {

    return NULL;
}

jintArray newArray(JNIEnv *env, jint size){
    jintArray mArr = env->NewIntArray(size);
    jint arr[size];
    for(int i=0;i<size;i++){
        arr[i] = i;
    }
    env->SetIntArrayRegion(mArr, 0, size, arr);
    return mArr;
}
void TransFormImg(int h, int w, int *cbuf, Mat myimg) {
    Mat image3(h, w, CV_8UC3, cbuf);
    int x, y;
    int tempH = myimg.rows;
    int tempW = myimg.cols;
    int len = tempH * tempW;
    BYTE *pCur = cbuf, *pRes = cbuf;
    int n[20];
    int d = n[0];
    n[0] = 2;
    for (y = 0; y < tempH; y++) {
        for (x = 0; x < tempW; x++, pCur++, pRes++) {
            pRes = dealWith(pCur, x, y); //f代表邻域运算的语句体(与位置x,y有关)
        }
    }
}
/*
 * Class:     com_zjy_js_customdialog_JniTest
 * Method:    getString
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jintArray JNICALL
Java_com_zjy_js_customdialog_opencvutils_ImageUtils_getModifyOrientation(JNIEnv *env, jclass type, jintArray buf, jint w,
                                                                         jint h) {
    jint *cbuf = NULL;
    if (buf == NULL) {
        LOGE( "input Array is null");
        jclass exCla = env->FindClass("com/zjy/js/customdialog/opencvutils/ExTest");
        env->ThrowNew(exCla, "errror,null data");
    }
    cbuf = env->GetIntArrayElements(buf, 0);
    if (cbuf == NULL) {
        LOGE("cbuf  ==null");
        return reinterpret_cast<jintArray>(JNI_ERR);
    }
    Mat myimg(h, w, CV_8UC4, cbuf);
    Mat target = myimg.clone();
    Mat cannyImg = myimg.clone();
    cvtColor(myimg, target, COLOR_BGRA2GRAY);
    Canny(target, cannyImg, 50, 150, 3);
//    IplImage *image = new IplImage(myimg);
//    IplImage* image3channel = change4channelTo3InIplImage(image);
//
//    IplImage* pCannyImage=cvCreateImage(cvGetSize(image3channel),IPL_DEPTH_8U,1);

//    cvCanny(image3channel,pCannyImage,50,150,3);

    int* outImage=new int[w*h];
    for(int i=0;i<w*h;i++)
    {
        outImage[i]=(int)pCannyImage->imageData[i];
    }

    int size = w * h;
    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, outImage);
    env->ReleaseIntArrayElements(buf, cbuf, 0);
    delete outImage;
    return result;
};

 Mat imgDataToMat(JNIEnv *env,jintArray buf, jint w,jint h){
    jint *cbuf;
    cbuf = env->GetIntArrayElements(buf, 0);
    Mat myimg(h, w, CV_8UC4, (unsigned char*) cbuf);
    return myimg;

}

//static void testImageRectification(cv::Mat &image_original)
//{
////    CV_SHOW(image_original); // CV_SHOW是cv::imshow的一个自定义宏，忽略即可
//    cv::Mat &&image = image_original.clone();
//
//    cv::Mat image_gray;
//    cv::cvtColor(image, image_gray, cv::COLOR_BGR2GRAY);
//    cv::threshold(image_gray, image_gray, g_threshVal, g_threshMax, cv::THRESH_BINARY);
//
//    std::vector< std::vector<cv::Point> > contours_list;
//    {
//        std::vector<cv::Vec4i> hierarchy;
//        // Since opencv 3.2 source image is not modified by this function
//        cv::findContours(image_gray, contours_list, hierarchy,
//                         cv::RetrievalModes::RETR_EXTERNAL, cv::ContourApproximationModes::CHAIN_APPROX_NONE);
//    }
//
//    for (uint32_t index = 0; index < contours_list.size(); ++index) {
//        cv::RotatedRect &&rect = cv::minAreaRect(contours_list[index]);
//        if (rect.size.area() > 1000) {
//            if (rect.angle != 0.) {
//                // 此处可通过cv::warpAffine进行旋转矫正，本例不需要
//            } //if
//
//            cv::Mat &mask = image_gray;
//            cv::drawContours(mask, contours_list, static_cast<int>(index), cv::Scalar(255), cv::FILLED);
//
//            cv::Mat extracted(image_gray.rows, image_gray.cols, CV_8UC1, cv::Scalar(0));
//            image.copyTo(extracted, mask);
////            CV_SHOW(extracted);
//
//            std::vector<cv::Point2f> poly;
//            cv::approxPolyDP(contours_list[index], poly, 30, true); // 多边形逼近，精度(即最小边长)设为30是为了得到4个角点
//            cv::Point2f pts_src[] = { // 此处顺序调整是为了和后面配对，仅作为示例
//                    poly[1],
//                    poly[0],
//                    poly[3],
//                    poly[2]
//            };
//
//            cv::Rect &&r = rect.boundingRect(); // 注意坐标可能超出图像范围
//            cv::Point2f pts_dst[] = {
//                    cv::Point(r.x, r.y),
//                    cv::Point(r.x + r.width, r.y),
//                    cv::Point(r.x + r.width, r.y + r.height) ,
//                    cv::Point(r.x, r.y + r.height)
//            };
//            cv::Mat &&M = cv::getPerspectiveTransform(pts_dst, pts_src); // 我这里交换了输入，因为后面指定了cv::WARP_INVERSE_MAP，你可以试试不交换的效果是什么
//
//            cv::Mat warp;cv::warpPerspective(image, warp, M, image.size(), cv::INTER_LINEAR + cv::WARP_INVERSE_MAP, cv::BORDER_REPLICATE);
////            CV_SHOW(warp);
//        } //if
//    }
//}
void testOpencv(){
    IplImage* srcImage=cvLoadImage(".\\face2.jpg",1);				//注意第二个参数的值
    CvSize ImageSize;
    ImageSize.height=srcImage->height;
    ImageSize.width=srcImage->width;
    IplImage* Image = cvCreateImage( ImageSize,srcImage->depth,1); //注意第三个参数的值
//    cvCvtColor(srcImage,Image,CV_RGB2GRAY);	  //这里的srcImage必须是指向三通道的彩色图片的指针，Image必须是单通道的灰度图片指针，不然会出现下面的错误
//    cvCanny(srcImage,Image,3,9,3);            //进行简单的边缘检测
//    cvNamedWindow("原图");
//    cvNamedWindow("效果图");
//    cvShowImage("原图",srcImage);
//    cvShowImage("效果图",Image);
//    cvWaitKey(0);
//    cvReleaseImage(&srcImage);
//    cvReleaseImage(&Image);
//    cvDestroyWindow("原图");
//    cvDestroyWindow("效果图");
}
JNIEXPORT void JNICALL Java_org_opencv_samples_tutorial2_Tutorial2Activity_FindFeatures(JNIEnv*, jobject, jlong addrGray, jlong addrRgba)
{
    Mat& mGr  = *(Mat*)addrGray;
    Mat& mRgb = *(Mat*)addrRgba;
    vector<KeyPoint> v;

    Ptr<FeatureDetector> detector = FastFeatureDetector::create(50);
    detector->detect(mGr, v);
    for( unsigned int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 10, Scalar(255,0,0,255));
    }
}

void onMouse(int event, int x, int y, int flags, void *utsc) {
    dstTri[0].x = 0;
    dstTri[0].y = 0;
    dstTri[1].x = image.rows - 1;
    dstTri[1].y = 0;
    dstTri[2].x = 0;
    dstTri[2].y = image.cols - 161;
    dstTri[3].x = image.rows - 1;
    dstTri[3].y = image.cols - 161;
    Mat transform = Mat::zeros(3, 3, CV_32FC1); //透视变换矩阵
    transform = getPerspectiveTransform(srcTri, dstTri);  //获取透视变换矩阵
    warpPerspective(image, imageWarp, transform, Size(image.rows, image.cols - 160));  //透视变换
}

#ifdef __cplusplus
}
#endif
#endif

