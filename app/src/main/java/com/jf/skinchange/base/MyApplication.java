package com.jf.skinchange.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.jf.hotfixlib.JFix;
import com.jf.router.api.RouterManager;
import com.jf.skinmanager.SkinManager;

import java.io.File;

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
        String path = Environment.getExternalStorageDirectory()+"/SkinChange/path.jar";
        File file = new File(path);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        JFix.installPatch(this,path);
    }
}
