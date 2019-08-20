package com.jf.skinchange.base;

import android.app.Application;

import com.jf.router.api.RouterManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RouterManager.getInstance().init(this);
    }
}
