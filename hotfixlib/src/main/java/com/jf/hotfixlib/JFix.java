package com.jf.hotfixlib;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class JFix {

    public static void installPatch(Context context,String path){

        try {
            ClassLoader classLoader = context.getClassLoader();
            //获取私有目录
           File cacheDir = context.getCacheDir();

            Field field = ShareReflectUtil.getFiled(classLoader,"pathList");
            // DexPathList
            Object pathList = field.get(classLoader);

            //获取dexElements;
            Field dexElementsField = ShareReflectUtil.getFiled(pathList,"dexElements");
            Object dexElements = dexElementsField.get(pathList);

            //4.4版本
            Method makeDexElements = ShareReflectUtil.getMethod(pathList,"makeDexElements");
            //optimizedDirectory:优化dex存放地址，必须是APP内置地址
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            //filelist
            Method splitDexPathMethod = ShareReflectUtil.getMethod(pathList,"splitDexPath");
            Object filelist = splitDexPathMethod.invoke(null, path, cacheDir);
            //执行
            Object patchDexElements =makeDexElements.invoke(pathList,filelist,cacheDir,suppressedExceptions);
            //6.0版本
            //Method makePathElements = ShareReflectUtil.getMethod(pathList,"makePathElements");
            //makePathElements.invoke(pathList,);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Object getPathElementsV4(Object pathList,String pathcPath,File optimizedDirectory) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method makeDexElements = ShareReflectUtil.getMethod(pathList,"makeDexElements");
        //optimizedDirectory:优化dex存放地址，必须是APP内置地址
        ArrayList<IOException> suppressedExceptions = new ArrayList<>();
        //filelist
        Method splitDexPathMethod = ShareReflectUtil.getMethod(pathList,"splitDexPath");
        Object filelist = splitDexPathMethod.invoke(null, pathcPath, optimizedDirectory);
        //执行
        Object patchDexElements =makeDexElements.invoke(pathList,filelist,optimizedDirectory,suppressedExceptions);
        return patchDexElements;
    }
}
