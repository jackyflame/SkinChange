package com.jf.router.api;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private Map<String,Class> routerMap = new HashMap<>();
    private Context mContext;

    private Router(){}

    private static class SingleHolder{
        private static Router instance = new Router();
    }

    public static Router getInstance(){
        return SingleHolder.instance;
    }


    public void init(Application application){
        this.mContext = application;
    }

    public void registRoute(String path,Class<?> cls){
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
