package com.jf.router.register;

import com.jf.modelb.BHomeActivity;
import com.jf.router.api.RouterManager;
import com.jf.router.api.interfaces.IRouterRegister;

public class BRouter implements IRouterRegister {

    @Override
    public void onRegist(RouterManager manager) {
        if(manager != null){
            manager.registRoute("/B/home", BHomeActivity.class);
        }
    }
}
