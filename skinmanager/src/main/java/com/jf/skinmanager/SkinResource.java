package com.jf.skinmanager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class SkinResource {

    private Context mContext;
    private Resources appRes;
    private Resources skinRes;
    private String skinPackName;
    private boolean isDefaultSkin;

    private SkinResource(){}

    private static class SingleHolder{
        private static final SkinResource INSTANCE = new SkinResource();
    }

    public static SkinResource getInstance(){
        return SingleHolder.INSTANCE;
    }

    public void init(Context context){
        this.mContext = context;
        this.appRes = mContext.getResources();
    }

    public void reset() {
        this.skinRes = null;
        this.skinPackName = "";
        this.isDefaultSkin = true;
    }

    public void applySkin(Resources skinRes,String packName){
        this.skinRes = skinRes;
        this.skinPackName = packName;
        this.isDefaultSkin = false;
    }

    public int getSkinIdentify(int resId){
        String resName = appRes.getResourceName(resId);
        String typeName = appRes.getResourceTypeName(resId);
        return skinRes.getIdentifier(resName,typeName,skinPackName);
    }

    public int getColor(int resId){
        if(isDefaultSkin){
            return appRes.getColor(resId);
        }
        int skinId = getSkinIdentify(resId);
        if(skinId == 0){
            return appRes.getColor(resId);
        }
        return skinRes.getColor(skinId);
    }

    public Object getBackground(int resId){
        String resourceTypeName = appRes.getResourceTypeName(resId);
        if("color".endsWith(resourceTypeName)){
            return getColor(resId);
        }else{
            return getDrawable(resId);
        }
    }

    public Drawable getDrawable(int resId){
        if(isDefaultSkin){
            return appRes.getDrawable(resId);
        }
        int skinId = getSkinIdentify(resId);
        if(skinId == 0){
            return appRes.getDrawable(resId);
        }
        return skinRes.getDrawable(skinId);
    }
}
