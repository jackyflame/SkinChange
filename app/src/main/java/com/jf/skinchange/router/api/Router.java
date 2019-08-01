package com.jf.skinchange.router.api;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private Map<String,Class> routerMap = new HashMap<>();

    private Router(){}

    private static class SingleHolder{
        public static Router instance = new Router();
    }

    public static Router getInstance(){
        return SingleHolder.instance;
    }

    public void registRoute(String path,Class<?> cls){
        routerMap.put(path,cls);
    }

    public void jump(String path){
        if(routerMap.get(path) != null){

        }
    }
}
