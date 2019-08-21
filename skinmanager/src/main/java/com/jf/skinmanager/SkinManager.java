package com.jf.skinmanager;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;
import java.util.Observable;

public class SkinManager extends Observable {

    private static Application mContext;
    private static SkinActivityLifecycleCallback skinCallback = new SkinActivityLifecycleCallback();

    private static class SingleHolder{
        private final static SkinManager instance = new SkinManager();
    }

    private SkinManager(){}

    public static SkinManager getInstantce(){
        return SingleHolder.instance;
    }

    public static void init(Application context){
        mContext = context;
        SkinResource.getInstance().init(context);
        mContext.registerActivityLifecycleCallbacks(skinCallback);
    }

    public void loadSkin(String skinPath){
        try {
            if(skinPath == null || skinPath.isEmpty()){
                SkinResource.getInstance().reset();
                SkinPrefrence.getInstance().reset();
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

            SkinResource.getInstance().applySkin(skinRes,packageName);
            SkinPrefrence.getInstance().setSkin(skinPath);

            setChanged();
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
