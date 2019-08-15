package com.jf.router.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

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

    }

    public void registRoute(String path,Class<? extends Activity> cls){
        routerMap.put(path,cls);
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
