package com.jf.skinchange.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.jf.modela.AHomeActivity;
import com.jf.modelb.BHomeActivity;
import com.jf.router.api.Router;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.getInstance().init(this);
        Router.getInstance().registRoute("/A/home", AHomeActivity.class);
        Router.getInstance().registRoute("/B/home", BHomeActivity.class);
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
