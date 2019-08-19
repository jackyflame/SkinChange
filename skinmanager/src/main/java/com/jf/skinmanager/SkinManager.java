package com.jf.skinmanager;

public class SkinManager {

    private static class SingleHolder{
        private final static SkinManager instance = new SkinManager();
    }
    private SkinManager(){}

    private SkinManager getInstantce(){
        return SingleHolder.instance;
    }


}
