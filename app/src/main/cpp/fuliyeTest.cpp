//
// Created by js on 2019/9/6.
//


#include "fuliyeTest.h"
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include "opencv2/core/cvstd.hpp"
#include "opencv2/core/types.hpp"
#include "opencv2/opencv.hpp"
#include "opencv2/imgcodecs/imgcodecs_c.h"
#include "opencv2/core/mat.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/core/fast_math.hpp"
#include "opencv2/core/base.hpp"
#include "opencv2/core/hal/interface.h"
#include "opencv2/core.hpp"
#include "AndroidLog.h"
#include "com_zjy_js_customdialog_JniTest.h"

#include <opencv2/core/ocl.hpp>
using namespace cv;
using namespace std;

#define GRAY_THRESH 150
#define HOUGH_VOTE 100
#define IMAGE_PATH "/sdcard/opencv"
#define COLSLIMIT 1080


class testCla{
protected:
    const void a();
    void a(uint b);
};

const void testCla::a() {

}
void testCla::a(uint b){

}
class cla2:testCla{
    void a() {

    }
};
//#define DEGREE 27
extern "C"{


class Sample{
public:
    const char *filepath = "/sdcard/";

    void createAlphaMat(Mat &mat)
    {

        size_t ia[3][4];
        size_t cnt = 0;
        for (auto &row:ia) {
            for (auto &col:row) {
                col = cnt;
                ++cnt;
            }
        }
        CV_Assert(mat.channels() == 4);
        for (int i = 0; i < mat.rows; ++i) {
            for (int j = 0; j < mat.cols; ++j) {
                Vec4b& bgra = mat.at<Vec4b>(i, j);
                bgra[0] = UCHAR_MAX; // Blue
                bgra[1] = saturate_cast<uchar>((float (mat.cols - j)) / ((float)mat.cols) * UCHAR_MAX); // Green
                bgra[2] = saturate_cast<uchar>((float (mat.rows - i)) / ((float)mat.rows) * UCHAR_MAX); // Red
                bgra[3] = saturate_cast<uchar>(0.5 * (bgra[1] + bgra[2])); // Alpha
            }
        }
    }

   int main(int argv, char **argc)
    {
        // Create mat with alpha channel
        Mat mat(480, 640, CV_8UC4);
        createAlphaMat(mat);
        vector<int> compression_params;
        compression_params.push_back(IMWRITE_PNG_COMPRESSION);
        compression_params.push_back(9);
        try {
            string str = filepath ;
            str = str + "alpha.png";
            imwrite(str, mat, compression_params);
        } catch (cv::Exception ex) {
            fprintf(stderr, "Exception converting image to PNG format: %s\n", ex.what());
            return 1;
        }
        fprintf(stdout, "Saved PNG file with alpha data.\n");
        return 0;
    }
};
}

#define MVersion JNI_VERSION_1_4

JavaVM *g_jvm;
using namespace cv::ocl;
JNIEXPORT jstring JNICALL getString()
{
    JNIEnv *env = NULL;
    g_jvm->AttachCurrentThread(&env, NULL); //g_jvm为JavaVM指针
    return env->NewStringUTF("This is Natvie String!");
}
JNIEXPORT jstring JNICALL getString2()
{
    JNIEnv *env = NULL;
    g_jvm->AttachCurrentThread(&env, NULL); //g_jvm为JavaVM指针
    return env->NewStringUTF("This is Natvie String2!");
}
JNIEXPORT void JNICALL testVoid() {
    JNIEnv *env = NULL;
    g_jvm->AttachCurrentThread(&env, NULL); //g_jvm为JavaVM指针
    jclass exCla = env->FindClass("com/zjy/js/customdialog/opencvutils/ExTest");
    env->ThrowNew(exCla, "errror void");
}
jintArray newArray1(JNIEnv *env, jint size){
    jintArray mArr = env->NewIntArray(size);
    jint *arr = env->GetIntArrayElements(mArr, JNI_FALSE);
    arr[0]=1;
    arr[1]=2;
    arr[3]=3;
    return mArr;
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
JNIEXPORT jintArray JNICALL getModify(jintArray buf, jint w,
                                   jint h) {
    LOGE("use getModify ");
    jint ret = 15;
    JNIEnv *env = NULL;
    g_jvm->AttachCurrentThread(&env, NULL); //g_jvm为JavaVM指针
//    return mArr;
//    return newArray1(env, 15);
    return newArray(env, 12);
}

JNIEXPORT jintArray JNICALL getModify2(jintArray buf, jint w,
                                      jint h) {
    LOGE("use getModify2 ");
    jint ret = 15;
//    jintArray *mArr =new jintArray[3] {
//            [0]=1, [1]=2, [2]=3
//    };
    JNIEnv *env = NULL;
    g_jvm->AttachCurrentThread(&env, NULL); //g_jvm为JavaVM指针
    jsize size=8;
//    return newArray1(env,size);
    return newArray(env, size);
}

struct JavaClassAndMethods {
    const char *claName;
//    JNINativeMethod methods[];
    JNINativeMethod *methods;
    int mLen;
};

void testPointer() {
    string a = "123123";
    size_t msize;
    char *p = const_cast<char*>(a.c_str());
    int *tp;
    int  b=100;
    tp = &b;
    LOGE("*p=%d,p=%p",*tp,tp);
}

void testArrIterate() {
    int var[3] = {1, 2, 3};
    int *ptr = var;  //指针中的数组地址
    for (int i = 0; i < 3; i++) {
        cout << *ptr << ',';
        ptr++;
    }
}
void testArrIterate2() {
    int var[3] = {1, 2, 3};
    int *ptr = var;  //指针中的数组地址
    for (int i = 0; i < 3; i++) {
        cout << *ptr << ',';
        ptr++;
    }
}

void testMalloc() {
    int *p = static_cast<int *>(malloc(sizeof(int)));
    *p=123;
    *p=421;
    int var[3] = {1, 2, 3};
    int *ptr = var;  //指针中的数组地址
    int size=sizeof(var)/ sizeof(var[0]);
    LOGE("var p=%d",(int)p);

    LOGE("var size=%d",size);
    for (int i = 0; i < 3; i++) {
        cout << *ptr << ',';
        LOGE("ptr=%d",*ptr);
        ptr++;
    }
    free(p);
}
template<typename T, typename T2>
void make2dArray(/*output para*/T** &arr, const uint16_t x, T2& volatileY_array)
{
    /*你要的长度。用模版的形式，就能够把数组以指针的形式传进来。*/
    int len = sizeof(volatileY_array)/sizeof(volatileY_array[0]);
    try
    {
        if (x != len)
        {
            throw std::bad_alloc();
        }
        arr = new T*[x];
        for (int i = 0; i < x; i++)
        {
            arr[i] = new T[volatileY_array[i]];
        }
    }
    catch (std::bad_alloc)
    {
        std::cout << "Error." << std::endl;
    }
}
JNIEXPORT jint JNICALL  JNI_OnLoad(JavaVM *vm, void *t) {
    LOGE("JNI_OnLoad invoked");
    JNIEnv* env;
    jint status =vm->GetEnv(reinterpret_cast<void**> (&env),MVersion);
    if (JNI_OK != status) {
        LOGW("JNI_OnLoad could not get JNI env");
        return JNI_ERR;
    }
    LOGE("JNI_OnLoad GetEnv OK ");
    g_jvm = vm;
    JNIEnv *envnow;
    const char *cla1 = "com/zjy/js/customdialog/opencvutils/ImageUtils";
    JNINativeMethod methods1[] = {
            [0]= {"getNativeString", "()Ljava/lang/String;", reinterpret_cast<void *>(getString)},
            [1]={"getNativeString2", "()Ljava/lang/String;", reinterpret_cast<void *>(getString2)}
            ,[2]={"getModifyOrientation2", "([III)[I",
                  reinterpret_cast<void *>(getModify2)}
            ,[3]={"testVoid", "()V",
                  reinterpret_cast<void *>(testVoid)}
       /*     ,[3]={"getModifyOrientation", "([III)[I",
                  reinterpret_cast<void *>(getModify)}*/
    };

    JavaClassAndMethods methodsAll[] = {
            [0].claName=cla1,
            [0].methods=methods1,
            [0].mLen=sizeof(methods1) / sizeof(JNINativeMethod)
//                [1].claName=cla2,
//                [1].methods=methods2
    };
    status = vm->AttachCurrentThread(&envnow, NULL);
    LOGE("AttachCurrentThread status=%d",status);
//    if(status < 0)
//    {
//        return status;
//    }
    int len1=sizeof(methodsAll);
    int len2=sizeof(JavaClassAndMethods);
    int len =len1/len2;
    LOGE("methods len1=%d,len2=%d,len=%d",len1,len2,len);
    for (int i = 0; i < len; i++)
    {
        JavaClassAndMethods temp = methodsAll[i];
        JNINativeMethod *tempMethods = temp.methods;
        const char* claName=temp.claName;
        string data=claName;
        const char *finalMSG = (data + " loaded").c_str();
        LOGE("%s loaded",claName);
        jclass mclas = envnow->FindClass(claName);
        int tL1 = sizeof(tempMethods);
        int tL2 = sizeof(tempMethods[0]);
//        int len3 = tL1 / tL2;
        int len3 = temp.mLen;
        LOGE(" register methods tL1=%d,tL2=%d, len3=%d",tL1,tL2,len3);
        envnow->RegisterNatives(mclas, tempMethods, len3);
    }
//    vm->DetachCurrentThread();
    return MVersion;
};

int fun(int *s) {
    int length = 0;
    while (true) {
        if (!s)
            break;
        s++;
    }
    return length;
}


TestHourf::TestHourf(int a, int b) {
    this->a=a;
    this->b=b;
}
void TestHourf::jiaozheng(){

};
class Child:TestHourf{
    /*int a;
    int b;*/
public :
    Child(int i, int i1) : TestHourf(i, i1) {
        TestHourf(i,i1);
}

void jiaozheng(){
        Mat imgOrigion = imread(IMAGE_PATH);
        Mat imgScale;
        float scaleFactor = COLSLIMIT / imgOrigion.cols;
        resize(imgOrigion, imgScale, Size(imgOrigion.cols * scaleFactor, imgOrigion.rows * scaleFactor));  // reduce image size to speed up calculation
        Mat imgGray;
        cvtColor(imgScale, imgGray, COLOR_BGR2GRAY);  // gray scale
        Mat imgCanny;
        Canny(imgGray, imgCanny, 100, 200);  // use canny operator to detect contour
        imshow("Contour detection", imgCanny);
        std::vector<Vec4i> lineAll;
        HoughLinesP(imgCanny, lineAll, 1, CV_PI / 180, 30, 50, 4);
        // draw all lines detected
        Mat imgAllLines;
        imgScale.copyTo(imgAllLines);
        for (int i = 0, steps = lineAll.size(); i < steps; i++)
        {
            line(imgAllLines, Point(lineAll[i][0], lineAll[i][1]), Point(lineAll[i][2], lineAll[i][3]), Scalar(255, 255, 255), 3, 8);
        }
    }
        IplImage *Rotate(IplImage *RowImage)
        {
            //建立储存边缘检测结果图像canImage
            IplImage *canImage=cvCreateImage(cvGetSize(RowImage),IPL_DEPTH_8U,1);
            //进行边缘检测
            cvCanny(RowImage,canImage,30,200,3);
            //进行hough变换
            CvMemStorage *storage=cvCreateMemStorage();
            CvSeq *lines=NULL;
            lines=cvHoughLines2(canImage,storage,CV_HOUGH_STANDARD,1,CV_PI/180,20,0,0);
            //统计与竖直夹角<30度的直线个数以及其夹角和
            int numLine=0;
            float sumAng=0.0;
            for(int i=0;i<lines->total;i++)
            {
                float *line=(float *)cvGetSeqElem(lines,i);
                float theta=line[1];  //获取角度 为弧度制
                if(theta<30*CV_PI/180 || (CV_PI-theta)<30*CV_PI/180 )
                {
                    numLine++;
                    sumAng=sumAng+theta;
                }
            }
            //计算出平均倾斜角，anAng为角度制
            float avAng=(sumAng/numLine)*180/CV_PI;

            //获取二维旋转的仿射变换矩阵
            CvPoint2D32f center;
            center.x=float (RowImage->width/2.0);
            center.y=float (RowImage->height/2.0);
            float m[6];
            CvMat M = cvMat( 2, 3, CV_32F, m );
            cv2DRotationMatrix( center,avAng,1, &M);
            //建立输出图像RotateRow
            double a=sin(sumAng/numLine);
            double b=cos(sumAng/numLine);
            int width_rotate=int (RowImage->height*fabs(a)+RowImage->width*fabs(b));
            int height_rotate=int (RowImage->width*fabs(a)+RowImage->height*fabs(b));
            IplImage *RotateRow=cvCreateImage(cvSize(width_rotate,height_rotate),IPL_DEPTH_8U,1);
            //变换图像，并用黑色填充其余值
            m[2]+=(width_rotate-RowImage->width)/2;
            m[5]+=(height_rotate-RowImage->height)/2;
            cvWarpAffine(RowImage,RotateRow, &M,CV_INTER_LINEAR+CV_WARP_FILL_OUTLIERS,cvScalarAll(0));
            //释放
            cvReleaseImage(&canImage);
            cvReleaseMemStorage(&storage);
            return RotateRow;
        }
};

int main(int argc, char **argv)
{
    //Read a single-channel image
    const char* filename = "imageText.jpg";
    Mat srcImg = imread(filename, CV_LOAD_IMAGE_GRAYSCALE);
    if(srcImg.empty())
        return -1;
    string mdir=dir;
    string imgPATH = mdir + "fuliye_read_gray.jpg";
    setUseOpenCL(true);
//    imwrite(imgPATH, srcImg);
    imshow("source", srcImg);

    Point center(srcImg.cols/2, srcImg.rows/2);

#ifdef DEGREE
    //Rotate source image
	Mat rotMatS = getRotationMatrix2D(center, DEGREE, 1.0);
	warpAffine(srcImg, srcImg, rotMatS, srcImg.size(), 1, 0, Scalar(255,255,255));
	imshow("RotatedSrc", srcImg);
	//imwrite("imageText_R.jpg",srcImg);
#endif

    //Expand image to an optimal size, for faster processing speed
    //Set widths of borders in four directions
    //If borderType==BORDER_CONSTANT, fill the borders with (0,0,0)
    Mat padded;
    int opWidth = getOptimalDFTSize(srcImg.rows);
    int opHeight = getOptimalDFTSize(srcImg.cols);
    copyMakeBorder(srcImg, padded, 0, opWidth-srcImg.rows, 0, opHeight-srcImg.cols, BORDER_CONSTANT, Scalar::all(0));

    Mat planes[] = {Mat_<float>(padded), Mat::zeros(padded.size(), CV_32F)};
    Mat comImg;
    //Merge into a double-channel image
    merge(planes,2,comImg);

    //Use the same image as input and output,
    //so that the results can fit in Mat well
    dft(comImg, comImg);

    //Compute the magnitude
    //planes[0]=Re(DFT(I)), planes[1]=Im(DFT(I))
    //magnitude=sqrt(Re^2+Im^2)
    split(comImg, planes);
    magnitude(planes[0], planes[1], planes[0]);

    //Switch to logarithmic scale, for better visual results
    //M2=log(1+M1)
    Mat magMat = planes[0];
    magMat += Scalar::all(1);
    log(magMat, magMat);

    //Crop the spectrum
    //Width and height of magMat should be even, so that they can be divided by 2
    //-2 is 11111110 in binary system, operator & make sure width and height are always even
    magMat = magMat(Rect(0, 0, magMat.cols & -2, magMat.rows & -2));

    //Rearrange the quadrants of Fourier image,
    //so that the origin is at the center of image,
    //and move the high frequency to the corners
    int cx = magMat.cols/2;
    int cy = magMat.rows/2;

    Mat q0(magMat, Rect(0, 0, cx, cy));
    Mat q1(magMat, Rect(0, cy, cx, cy));
    Mat q2(magMat, Rect(cx, cy, cx, cy));
    Mat q3(magMat, Rect(cx, 0, cx, cy));

    Mat tmp;
    q0.copyTo(tmp);
    q2.copyTo(q0);
    tmp.copyTo(q2);

    q1.copyTo(tmp);
    q3.copyTo(q1);
    tmp.copyTo(q3);

    //Normalize the magnitude to [0,1], then to[0,255]
    normalize(magMat, magMat, 0, 1, CV_MINMAX);
    Mat magImg(magMat.size(), CV_8UC1);
    magMat.convertTo(magImg,CV_8UC1,255,0);
    imshow("magnitude", magImg);
    //imwrite("imageText_mag.jpg",magImg);

    //Turn into binary image
    threshold(magImg,magImg,GRAY_THRESH,255,CV_THRESH_BINARY);
    imshow("mag_binary", magImg);
    //imwrite("imageText_bin.jpg",magImg);

    //Find lines with Hough Transformation
    vector<Vec2f> lines;
    float pi180 = (float)CV_PI/180;
    Mat linImg(magImg.size(),CV_8UC3);
    HoughLines(magImg,lines,1,pi180,HOUGH_VOTE,0,0);
    int numLines = lines.size();
    for(int l=0; l<numLines; l++)
    {
        float rho = lines[l][0], theta = lines[l][1];
        Point pt1, pt2;
        double a = cos(theta), b = sin(theta);
        double x0 = a*rho, y0 = b*rho;
        pt1.x = cvRound(x0 + 1000*(-b));
        pt1.y = cvRound(y0 + 1000*(a));
        pt2.x = cvRound(x0 - 1000*(-b));
        pt2.y = cvRound(y0 - 1000*(a));
        line(linImg,pt1,pt2,Scalar(255,0,0),3,8,0);
    }
    imshow("lines",linImg);
    //imwrite("imageText_line.jpg",linImg);
    if(lines.size() == 3){
        //cout << "found three angels:" << endl;
        //cout << lines[0][1]*180/CV_PI << endl << lines[1][1]*180/CV_PI << endl << lines[2][1]*180/CV_PI << endl << endl;
    }

    //Find the proper angel from the three found angels
    float angel=0;
    float piThresh = (float)CV_PI/90;
    float pi2 = CV_PI/2;
    for(int l=0; l<numLines; l++)
    {
        float theta = lines[l][1];
        if(abs(theta) < piThresh || abs(theta-pi2) < piThresh)
            continue;
        else{
            angel = theta;
            break;
        }
    }

    //Calculate the rotation angel
    //The image has to be square,
    //so that the rotation angel can be calculate right
    angel = angel<pi2 ? angel : angel-CV_PI;
    if(angel != pi2){
        float angelT = srcImg.rows*tan(angel)/srcImg.cols;
        angel = atan(angelT);
    }
    float angelD = angel*180/(float)CV_PI;
    ////cout << "the rotation angel to be applied:" << endl << angelD << endl << endl;

    //Rotate the image to recover
    Mat rotMat = getRotationMatrix2D(center,angelD,1.0);
    Mat dstImg = Mat::ones(srcImg.size(),CV_8UC3);
    warpAffine(srcImg,dstImg,rotMat,srcImg.size(),1,0,Scalar(255,255,255));
    imshow("result",dstImg);
    //imwrite("imageText_D.jpg",dstImg);

    waitKey(0);

    return 0;
}