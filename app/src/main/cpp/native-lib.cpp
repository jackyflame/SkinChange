//
// Created by yh162 on 2019/8/27.
//
#include<jni.h>
#include <stdio.h>
#include <stdlib.h>

extern "C"
void JNICALL
Java_com_jf_skinchange_ui_main_HomeActivity_writeTest(JNIEnv *env, jobject thiz, jstring msg){
    printf("%s",msg);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_jf_skinchange_ui_main_HomeActivity_readTest(JNIEnv *env, jobject thiz) {
    // TODO: implement readTest()
    return (jstring) "23333";
}