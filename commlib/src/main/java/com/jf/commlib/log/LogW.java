package com.jf.commlib.log;

import android.util.Log;

public class LogW {

    public static final String TAG = "JF.ROUTER";

    public static void i(String msg){
        Log.i(TAG,msg);
    }

    public static void i(String title, String msg){
        Log.i(TAG, title +" >>> "+ msg);
    }

    public static void d(String msg){
        Log.d(TAG,msg);
    }

    public static void d(String title, String msg){
        Log.d(TAG, title +" >>> " + msg);
    }

    public static void e(String msg){
        Log.e(TAG,msg);
    }

    public static void e(String title, String msg){
        Log.e(TAG, title + " >>> " + msg);
    }

}
