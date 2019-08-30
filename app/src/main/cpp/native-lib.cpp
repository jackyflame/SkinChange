//
// Created by yh162 on 2019/8/27.
//
#include<jni.h>
#include <string>

const char* cacheMsg;
jstring msgParam;

extern "C"
void JNICALL
Java_com_jf_skinchange_ui_main_HomeActivity_writeTest(JNIEnv *env, jobject thiz, jstring msg){
    jboolean isCopy;
    cacheMsg = env->GetStringUTFChars(msg, &isCopy);
    msgParam = msg;
    printf("isCopy:%d\n",isCopy);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_jf_skinchange_ui_main_HomeActivity_readTest(JNIEnv *env, jobject thiz) {
    if(cacheMsg == NULL){
        return NULL;
    }
    char buff[128] = {0};
    sprintf(buff,"hello %s",cacheMsg);

    std::string hello = strcat("111","233333");
    return env->NewStringUTF(hello.c_str());
}
