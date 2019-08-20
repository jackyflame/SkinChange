package com.jf.skinmanager;

import android.content.Context;
import android.content.SharedPreferences;

public class SkinPrefrence {

    private SharedPreferences mSp;

    private SkinPrefrence(){}

    private static class SingleHolder{
        private static final SkinPrefrence INSTANCE = new SkinPrefrence();
    }

    public static SkinPrefrence getInstance(){
        return SingleHolder.INSTANCE;
    }

    public static void init(Context context){
        getInstance().mSp = context.getSharedPreferences("SkinPrefrence", Context.MODE_PRIVATE);
    }

    public void setSkin(String skinPath){
        mSp.edit().putString("SKIN_PATH",skinPath).apply();
    }

    public void reset() {
        mSp.edit().remove("SKIN_PATH").apply();
    }

}
