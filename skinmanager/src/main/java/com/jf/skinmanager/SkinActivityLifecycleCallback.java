package com.jf.skinmanager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;

public class SkinActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        SkinViewFactory factory = new SkinViewFactory();
        SkinManager.getInstantce().addObserver(factory);
        LayoutInflater.from(activity).setFactory2(factory);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
