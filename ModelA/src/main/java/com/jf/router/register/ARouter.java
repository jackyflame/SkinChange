package com.jf.router.register;

import com.jf.modela.AHomeActivity;
import com.jf.router.api.RouterManager;
import com.jf.router.api.interfaces.IRouterRegister;

public class ARouter implements IRouterRegister {

    @Override
    public void onRegist(RouterManager manager) {
        if(manager != null){
            manager.registRoute("/A/home", AHomeActivity.class);
        }
    }
}
