package com.jf.skinchange.livedata;

import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

public class LiveDataBus_Ver1 {

    private final Map<String, MutableLiveData<Object>> bus;

    private LiveDataBus_Ver1(){
        bus = new HashMap<>();
    }

    private static class SingletonHolder {
        /***单例对象实例*/
        static final LiveDataBus_Ver1 INSTANCE = new LiveDataBus_Ver1();
    }

    public static LiveDataBus_Ver1 get() {
        return LiveDataBus_Ver1.SingletonHolder.INSTANCE;
    }

    public MutableLiveData<Object> with(String key){
        return with(key, Object.class);
    }

    public <T> MutableLiveData<T> with(String key, Class<T> clz){
        //如果不存在，则直接新建一个LiveData
        if(!bus.containsKey(key)){
            MutableLiveData<Object> liveData = new MutableLiveData<>();
            bus.put(key,liveData);
        }
        //返回给用户调用
        return (MutableLiveData<T>) bus.get(key);
    }

}
