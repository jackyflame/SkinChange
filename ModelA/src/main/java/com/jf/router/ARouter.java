package com.jf.router;

import android.app.Activity;

import com.jf.modela.AHomeActivity;
import com.jf.router.api.interfaces.IRouterRegister;

import java.util.Map;

public class ARouter implements IRouterRegister {

    @Override
    public void onRegist(Map<String, Class<? extends Activity>> map) {
        if(map != null){
            map.put("/A/home", AHomeActivity.class);
        }
    }
}
