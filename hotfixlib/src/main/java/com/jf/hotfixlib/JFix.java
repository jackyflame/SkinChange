package com.jf.hotfixlib;

import android.content.Context;

import java.lang.reflect.Field;

public class JFix {

    public static void installPatch(Context context,String path){
        ClassLoader classLoader = context.getClassLoader();

        try {
            Field field = ShareReflectUtil.getFiled(classLoader,"pathList");
            // DexPathList
            Object pathList = field.get(classLoader);

            //

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

}
