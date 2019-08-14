package com.jf.skinchange.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.jf.modela.AHomeActivity;
import com.jf.modelb.BHomeActivity;
import com.jf.router.api.RouterManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RouterManager.getInstance().init(this);
        RouterManager.getInstance().registRoute("/A/home", AHomeActivity.class);
        RouterManager.getInstance().registRoute("/B/home", BHomeActivity.class);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
            @Override
            public void onActivityStarted(Activity activity) {}
            @Override
            public void onActivityResumed(Activity activity) {}
            @Override
            public void onActivityPaused(Activity activity) {}
            @Override
            public void onActivityStopped(Activity activity) {}
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
            @Override
            public void onActivityDestroyed(Activity activity) {}
        });
    }
}
