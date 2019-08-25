package com.jf.skinmanager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.jf.commlib.log.LogW;

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
        String resEntryName = appRes.getResourceEntryName(resId);
        //String resName = appRes.getResourceName(resId);
        String typeName = appRes.getResourceTypeName(resId);
        int skinId = skinRes.getIdentifier(resEntryName,typeName,skinPackName);
        LogW.d("getSkinIdentify","resEntryName:"+resEntryName + " typeName:"+typeName + " skinPackName:"+skinPackName
                + " from "+Integer.toHexString(resId) + " to "+ Integer.toHexString(skinId));
        return skinId;
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

    public ColorStateList getColorStateList(int resId){
        if(isDefaultSkin){
            return appRes.getColorStateList(resId);
        }
        int skinId = getSkinIdentify(resId);
        if(skinId == 0){
            return appRes.getColorStateList(resId);
        }
        return skinRes.getColorStateList(skinId);
    }

    public Object getBackground(int resId){
        String resourceTypeName = appRes.getResourceTypeName(resId);
        LogW.d("getBackground","resourceTypeName:"+resourceTypeName );
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
