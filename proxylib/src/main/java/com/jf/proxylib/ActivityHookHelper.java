package com.jf.proxylib;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ActivityHookHelper {

    private static final String TAG = "ActivityHookHelper";
    private static final String EXTRA_RAW = "EXTRA_RAW";

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
            Class<?> SingletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = SingletonClass.getField("mInstance");
            mInstanceField.setAccessible(true);
            //IActivityManager实例
            final Object iam = mInstanceField.get(signleIA);
            //动态代理
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{Class.forName("android.app.IActivityManager")}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    int index = -1;
                    Intent raw = null;
                    if("startActivity".equals(method.getName())){
                        Log.d(TAG,"startActivity 准备启动");
                        for(int i =0;i<args.length;i++){
                            if(args[i] instanceof Intent){
                                index = i;
                                raw = (Intent) args[i];
                            }
                        }

                        //替换
                        Log.d(TAG,"invoke raw: 替换 ");
                        Intent newIntent = new Intent();
                        newIntent.setComponent(new ComponentName("com.jf.proxylib", TargetActivity.class.getName()));
                        newIntent.putExtra(EXTRA_RAW,raw);

                        args[index] = newIntent;

                    }

                    return method.invoke(iam,args);
                }
            });

            mInstanceField.set(singleton,proxy);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
