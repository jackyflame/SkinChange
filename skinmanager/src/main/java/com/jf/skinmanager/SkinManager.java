package com.jf.skinmanager;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.jf.commlib.log.LogW;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

public class SkinManager extends Observable {

    private static Application mContext;
    private static SkinActivityLifecycleCallback skinCallback;

    private static class SingleHolder{
        private final static SkinManager instance = new SkinManager();
    }

    private SkinManager(){}

    public static SkinManager getInstantce(){
        return SingleHolder.instance;
    }

    public static void init(Application application){
        mContext = application;
        SkinPrefrence.init(application);
        SkinResource.getInstance().init(application);
        skinCallback = new SkinActivityLifecycleCallback();
        application.registerActivityLifecycleCallbacks(skinCallback);
        if(!SkinResource.getInstance().isDefaultSkin()){
            SkinManager.getInstantce().loadSkin(SkinPrefrence.getInstance().getSkin());
        }
        LogW.e("SkinManager","---------init success---------");
    }

    public void loadSkin(String skinPath){
        try {
            if(skinPath == null || skinPath.isEmpty()){
                SkinResource.getInstance().reset();
                SkinPrefrence.getInstance().reset();
                LogW.e("loadSkin","error: skinPath is empty!");
                return;
            }
            File file = new File(skinPath);
            if(!file.exists()){
                file.getParentFile().mkdirs();
                LogW.e("loadSkin","error: file is not exists!");
                return;
            }
            Resources appRes = mContext.getResources();
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getMethod("addAssetPath",String.class);
            method.invoke(assetManager,skinPath);
            Resources skinRes= new Resources(assetManager,appRes.getDisplayMetrics(),appRes.getConfiguration());
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo skinPackageInfo = packageManager.getPackageArchiveInfo(skinPath,PackageManager.GET_ACTIVITIES);
            String packageName = skinPackageInfo.packageName;
            LogW.e("loadSkin","packageName :"+packageName);
            SkinResource.getInstance().applySkin(skinRes,packageName);
            SkinPrefrence.getInstance().setSkin(skinPath);

            setChanged();
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }
}
