package com.jf.skinmanager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.jf.commlib.log.LogW;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SkinActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    private Map<String,SkinViewFactory> factoryMap = new HashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        try {
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            LogW.d("onActivityCreated","LayoutInflater for real:" + layoutInflater);
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater,false);
            SkinViewFactory factory = new SkinViewFactory();
            layoutInflater.setFactory2(factory);
            SkinManager.getInstantce().addObserver(factory);
            factoryMap.put(activity.toString(),factory);
            LogW.d("onActivityCreated","SkinManager add key>"+activity.toString() + " factory>" + factory);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if(factoryMap.containsKey(activity.toString())){
            SkinViewFactory factory = factoryMap.get(activity.toString());
            SkinManager.getInstantce().deleteObserver(factory);
            LogW.d("onActivityCreated","SkinManager remove key>"+activity.toString() + " factory>" + factory);
        }
    }
}
