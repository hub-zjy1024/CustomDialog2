//
// Created by js on 2019/9/6.
//


#include "fuliyeTest.h"

using namespace cv;
using namespace std;

#define GRAY_THRESH 150
#define HOUGH_VOTE 100
#define IMAGE_PATH "/sdcard/opencv"
#define COLSLIMIT 1080

//#define DEGREE 27


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
    TestHourf ho=TestHourf(1,2);
    TestHourf ho2(1,2);
//    int b=ho->a;
    ho.jiaozheng();
    //Read a single-channel image
    const char* filename = "imageText.jpg";
    Mat srcImg = imread(filename, CV_LOAD_IMAGE_GRAYSCALE);
    if(srcImg.empty())
        return -1;
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


