package com.jf.router;

import android.app.Activity;

import com.jf.modelb.BHomeActivity;
import com.jf.router.api.interfaces.IRouterRegister;

import java.util.Map;

public class BRouter implements IRouterRegister {

    @Override
    public void onRegist(Map<String, Class<? extends Activity>> map) {
        if(map != null){
            map.put("/B/home", BHomeActivity.class);
        }
    }
}
