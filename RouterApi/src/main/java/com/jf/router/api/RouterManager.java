package com.jf.router.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jf.commlib.log.LogW;
import com.jf.router.api.interfaces.IRouterRegister;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RouterManager {

    private Map<String,Class<? extends Activity>> routerMap = new HashMap<>();
    private Context mContext;

    private RouterManager(){}

    private static class SingleHolder{
        private static RouterManager instance = new RouterManager();
    }

    public static RouterManager getInstance(){
        return SingleHolder.instance;
    }


    public void init(Application application){
        this.mContext = application;
        //通过反射自动注册指定包实现了接口的类
        Set<String> clazzSet =  ClassUtils.getClassesFromMultedex(application,"com.jf.router.register");
        try {
            Iterator<String> iterator = clazzSet.iterator();
            while (iterator.hasNext()){
                String clazzName = iterator.next();
                //LogW.d("RouterManager","getClasses item:"+clazzName);
                Class<?> clazz = Class.forName(clazzName);
                if(IRouterRegister.class.isAssignableFrom(clazz)){
                    Method method = clazz.getMethod("onRegist",RouterManager.class);
                    //LogW.d("RouterManager","getMethod:"+method);
                    method.invoke(clazz.newInstance(),getInstance());
                    LogW.d("RouterManager","getClassesFromPackgeName item:"+clazzName + " invoke onRegist()");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registRoute(String path,Class<? extends Activity> cls){
        routerMap.put(path,cls);
        LogW.d("RouterManager","registRoute path:"+path+" cls:"+cls.getName());
    }

    public void jump(String path){
        if(routerMap.get(path) != null){
            Class<?> cls = routerMap.get(path);
            Intent intent = new Intent(mContext,cls);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }
    }
}
