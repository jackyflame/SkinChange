package com.jf.proxylib;

import android.os.Build;

import java.lang.reflect.Field;

public class ActivityHookHelper {

    public static void invokeIntent(){
        try {
            //获取IActivityManager 单例类
            Field singleton = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Class<?> activityManager = Class.forName("android.app.ActivityManager");
                singleton = activityManager.getDeclaredField("IActivityManagerSingleton");
            }else{
                Class<?> activityManager = Class.forName("android.app.ActivityManagerNative");
                singleton = activityManager.getDeclaredField("gDefault");
            }
            singleton.setAccessible(true);
            Object signleIA = singleton.get(null);
            //获取单例类中的IAM实现
            Class<?> Singleton = Class.forName("android.util.Singleton");
            Field mInstance = Singleton.getField("mInstance");
            Object iam = mInstance.get(signleIA);//IActivityManager实例

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
