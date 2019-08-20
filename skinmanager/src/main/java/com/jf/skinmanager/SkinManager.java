package com.jf.skinmanager;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

public class SkinManager {

    private static Application mContext;

    private static class SingleHolder{
        private final static SkinManager instance = new SkinManager();
    }

    private SkinManager(){}

    private SkinManager getInstantce(){
        return SingleHolder.instance;
    }

    public static void init(Application context){
        mContext = context;
    }

    public void loadSkin(String skinPath){
        Resources appRes = mContext.getResources();
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getMethod("addAssetPath",String.class);
            method.invoke(assetManager,skinPath);
            Resources skinRes= new Resources(assetManager,appRes.getDisplayMetrics(),appRes.getConfiguration());
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo skinPackageInfo = packageManager.getPackageArchiveInfo(skinPath,PackageManager.GET_ACTIVITIES);
            String packageName = skinPackageInfo.packageName;
            SkinResource.getInstance().applySkin(skinRes,packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
