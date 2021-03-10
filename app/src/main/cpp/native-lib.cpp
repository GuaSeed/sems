#include <jni.h>
#include <string>
#include <iostream>
#include <android/native_window_jni.h>
#include <android/bitmap.h>
#include "cpp_log.h"
#include "opencv2/opencv.hpp"
#include "ean_13.h"
#include "barcode.h"

ANativeWindow *window = 0;
using namespace cv;
using namespace std;

extern "C"
JNIEXPORT jobject JNICALL
Java_cool_zzy_sems_application_OpencvJni_haveBarcode(JNIEnv *env, jobject instance,jbyteArray data_, jint imgwidth, jint imgheight) {
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    Mat src(imgheight*3/2, imgwidth, CV_8UC1, data);
    cvtColor(src, src, COLOR_YUV2RGBA_NV21);
    rotate(src, src, ROTATE_90_CLOCKWISE);
    cv::Mat temp;
    int SIZE = 2;
    cv::resize(src, temp, Size(src.cols / SIZE, src.rows / SIZE));
    cv::Rect rect = find(temp);
    rect.x = rect.x * SIZE;
    rect.y = rect.y * SIZE;
    rect.width = rect.width * SIZE;
    rect.height = rect.height * SIZE;
    rectangle(src, rect, Scalar(255, 0, 0), 2);
    if (window) {
            ANativeWindow_setBuffersGeometry(window, src.cols, src.rows, WINDOW_FORMAT_RGBA_8888);
            ANativeWindow_Buffer window_buffer;
            do {
                //lock失败 直接brek出去
                if (ANativeWindow_lock(window, &window_buffer, 0)) {
                    ANativeWindow_release(window);
                    window = 0;
                    break;
                }

                uint8_t *dst_data = static_cast<uint8_t *>(window_buffer.bits);
                //stride : 一行多少个数据
                //（RGBA） * 4
                int dst_linesize = window_buffer.stride * 4;

                //一行一行拷贝，src.data是图片的RGBA数据，要拷贝到dst_data中，也就是window的缓冲区里
                for (int i = 0; i < window_buffer.height; ++i) {
                    memcpy(dst_data + i * dst_linesize, src.data + i * src.cols * 4, dst_linesize);
                }
                //提交刷新
                ANativeWindow_unlockAndPost(window);
            } while (0);
        }
    jclass rect_class = env->FindClass("cool/zzy/sems/application/model/Rect");
    jmethodID rect_mid   = env->GetMethodID(rect_class,"<init>","()V");
    jfieldID  rect_fid_x = env->GetFieldID(rect_class,"x","I");
    jfieldID  rect_fid_y = env->GetFieldID(rect_class,"y","I");
    jfieldID  rect_fid_width = env->GetFieldID(rect_class,"width","I");
    jfieldID  rect_fid_height = env->GetFieldID(rect_class,"height","I");
    jobject rect_obj = env->NewObject(rect_class, rect_mid);
    env->SetIntField(rect_obj, rect_fid_x, rect.x);
    env->SetIntField(rect_obj, rect_fid_y, rect.y);
    env->SetIntField(rect_obj, rect_fid_width, rect.width);
    env->SetIntField(rect_obj, rect_fid_height, rect.height);
    env->ReleaseByteArrayElements(data_, data, 0);
    src.release();
    return rect_obj;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_cool_zzy_sems_application_OpencvJni_recognitionBarcode(JNIEnv *env, jobject instance, jobject bmp,
                                                   jint rect_x,jint rect_y,jint rect_width,jint rect_height) {
    AndroidBitmapInfo bitmapInfo;
    void *pixelscolor;
    int ret;
    //获取图像信息，如果返回值小于0就是执行失败
    if ((ret = AndroidBitmap_getInfo(env, bmp, &bitmapInfo)) < 0) {
        LOGI("AndroidBitmap_getInfo failed! error-%d", ret);
        return NULL;
    }

    //判断图像类型是不是RGBA_8888类型
    if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGI("BitmapInfoFormat error");
        return NULL;
    }

    //获取图像像素值
    if ((ret = AndroidBitmap_lockPixels(env, bmp, &pixelscolor)) < 0) {
        LOGI("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    //生成源图像
    cv::Mat src(bitmapInfo.height, bitmapInfo.width, CV_8UC4, pixelscolor);
    rotate(src, src, ROTATE_90_CLOCKWISE);
    cv::Rect rect(rect_x,rect_y,rect_width,rect_height);
    if(rect.area() < 20000){
        return env->NewStringUTF("");
    }
    return env->NewStringUTF(ean_13::recognition(src, rect).data());
}
extern "C"
JNIEXPORT void JNICALL
Java_cool_zzy_sems_application_OpencvJni_setSurface(JNIEnv *env, jobject instance, jobject surface) {
    if (window) {
        ANativeWindow_release(window);
        window = 0;
    }
    window = ANativeWindow_fromSurface(env, surface);
}