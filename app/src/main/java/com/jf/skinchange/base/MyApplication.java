package com.jf.skinchange.base;

import android.app.Application;
import android.content.Context;

import com.jf.hotfixlib.JFix;
import com.jf.router.api.RouterManager;
import com.jf.skinmanager.SkinManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RouterManager.getInstance().init(this);
        SkinManager.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        JFix.installPatch(this,"");
    }
}
