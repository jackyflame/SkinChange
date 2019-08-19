package com.jf.skinchange.proxy.http;

public class HttpProxy implements IHttp{

    private static HttpProxy mInstantc;
    private IHttp mHttp;

    public static HttpProxy obtain() {
        if(mInstantc == null){
            synchronized (HttpProxy.class){
                if(mInstantc == null){
                    mInstantc = new HttpProxy();
                }
            }
        }
        return mInstantc;
    }

    public void init(IHttp mHttp){
        this.mHttp = mHttp;
    }

    @Override
    public void get(String url, ICallback callback) {
        mHttp.get(url,callback);
    }

    @Override
    public void post(String url, ICallback callback) {
        mHttp.post(url,callback);
    }
}
