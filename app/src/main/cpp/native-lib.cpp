//
// Created by yh162 on 2019/8/27.
//
#include<jni.h>
#include <string>
#include <sys/mman.h>
#include <fcntl.h>
#include <android/log.h>
#include <unistd.h>

int m_fd,m_size;
int8_t* m_ptr;

#define  LOG(...) __android_log_print(ANDROID_LOG_ERROR,"JF.ROUTER.JNI",__VA_ARGS__);

extern "C"
void JNICALL
Java_com_jf_skinchange_ui_main_HomeActivity_writeTest(JNIEnv *env, jobject thiz, jstring msg){

    std::string filePath = "/sdcard/mmapTest.txt";
    m_fd = open(filePath.c_str(),O_RDWR|O_CREAT,S_IRWXU);
    //获取一页内存，linux采用分页来管理内存，内存管理中以页为单位，一般32位Linux系统为4096个字节
    m_size = getpagesize();
    //设置文件大小:m_size
    ftruncate(m_fd,m_size);
    //映射文件
    m_ptr = (int8_t *)mmap(0,m_size,PROT_READ|PROT_WRITE,MAP_SHARED,m_fd,0);

    std::string myStr("文件写入成功啦!!!");

    memcpy(m_ptr,myStr.data(),myStr.size());

    LOG("写入数据:%s",myStr.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_jf_skinchange_ui_main_HomeActivity_readTest(JNIEnv *env, jobject thiz) {

    std::string filePath = "/sdcard/mmapTest.txt";
    //打开文件
    m_fd = open(filePath.c_str(),O_RDWR|O_CREAT,S_IRWXU);
    //映射文件
    m_ptr = (int8_t *)mmap(0,m_size,PROT_READ,MAP_SHARED,m_fd,0);

    memcpy()

    return env->NewStringUTF("");
}
