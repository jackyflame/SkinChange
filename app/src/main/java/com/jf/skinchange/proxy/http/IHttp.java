package com.jf.skinchange.proxy.http;

public interface IHttp {

    void get(String url,ICallback callback);

    void post(String url,ICallback callback);

}
