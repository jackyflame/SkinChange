package com.jf.router.api;

import android.content.Context;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import dalvik.system.DexFile;

public class ClassUtils {

    public static Set<String> getClassesFromPackgeName(Context context,String packgeName){
        Set<String> set = new HashSet<>();
        try {
            //apk的dex文件，获得源码来反射
            DexFile dex = new DexFile(context.getPackageCodePath());
            //枚举类，来遍历所有源代码
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()){
                String className = entries.nextElement();
                if(className.contains(packgeName)){
                    set.add(className);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return set;
    }

}
