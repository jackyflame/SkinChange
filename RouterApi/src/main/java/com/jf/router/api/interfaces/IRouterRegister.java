package com.jf.router.api.interfaces;

import android.app.Activity;

import java.util.Map;

public interface IRouterRegister {

    void onRegist(Map<String,Class<? extends Activity>> map);

}
