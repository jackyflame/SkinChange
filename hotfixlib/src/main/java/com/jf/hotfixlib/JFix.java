package com.jf.hotfixlib;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class JFix {

    public static void installPatch(Context context, String path) {

        try {
            ClassLoader classLoader = context.getClassLoader();
            //获取私有目录
            File cacheDir = context.getCacheDir();

            Field field = ShareReflectUtil.getFiled(classLoader, "pathList");
            // DexPathList
            Object pathList = field.get(classLoader);
            if(pathList == null){
                Log.e("JFix","find Field pathList error!!");
                return;
            }
            //获取dexElements;
            Field dexElementsField = ShareReflectUtil.getFiled(pathList, "dexElements");
            Object[] dexElements = (Object[]) dexElementsField.get(pathList);
            if(dexElements == null){
                Log.e("JFix","find Field dexElements error!!");
                return;
            }


            Object[] pathElements = null;
            //4.4之前版本
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
                pathElements = getPathElementsOld(pathList,path,cacheDir);
            //4.4以后
            }else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                pathElements = getPathElementsV4(pathList,path,cacheDir);
            }else{
                pathElements = getPathElementsV6(pathList,path,cacheDir);
            }
            if(pathElements == null){
                Log.e("JFix","find Field path-elements-list error!!");
                return;
            }

            if(dexElements.getClass().getComponentType() == null){
                Log.e("JFix"," dexElements getComponentType error: rst is null!!");
                return;
            }
            //添加到列表中
            Object[] fixElements = (Object[]) Array.newInstance(dexElements.getClass().getComponentType(),dexElements.length + pathElements.length);
            System.arraycopy(pathElements,0, fixElements,0, pathElements.length);
            System.arraycopy(dexElements,0, fixElements,pathElements.length, dexElements.length);

            dexElementsField.set(pathList,fixElements);

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

    private static Object[] getPathElementsOld(Object pathList, String patchPath, File optimizedDirectory)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //获取makeDexElements方法
        Method makeDexElements = ShareReflectUtil.getMethod(pathList, "makeDexElements");
        //fileList
        Method splitDexPathMethod = ShareReflectUtil.getMethod(pathList, "splitDexPath");
        Object fileList = splitDexPathMethod.invoke(null, patchPath, optimizedDirectory);
        //执行
        Object patchDexElements = makeDexElements.invoke(null, fileList, optimizedDirectory);
        return (Object[]) patchDexElements;
    }

    private static Object[] getPathElementsV4(Object pathList, String patchPath, File optimizedDirectory)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //获取makeDexElements方法
        Method makeDexElements = ShareReflectUtil.getMethod(pathList, "makeDexElements");
        //optimizedDirectory:优化dex存放地址，必须是APP内置地址
        ArrayList<IOException> suppressedExceptions = new ArrayList<>();
        //fileList
        Method splitDexPathMethod = ShareReflectUtil.getMethod(pathList, "splitDexPath");
        Object fileList = splitDexPathMethod.invoke(null, patchPath, optimizedDirectory);
        //执行
        Object patchDexElements = makeDexElements.invoke(null, fileList, optimizedDirectory, suppressedExceptions);
        return (Object[]) patchDexElements;
    }

    private static Object[] getPathElementsV6(Object pathList, String patchPath, File optimizedDirectory)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //获取makePathElements方法
        Method makeDexElements = ShareReflectUtil.getMethod(pathList, "makePathElements");
        //optimizedDirectory:优化dex存放地址，必须是APP内置地址
        ArrayList<IOException> suppressedExceptions = new ArrayList<>();
        //fileList
        Method splitDexPathMethod = ShareReflectUtil.getMethod(pathList, "splitDexPath");
        Object fileList = splitDexPathMethod.invoke(null, patchPath);
        //执行
        Object patchDexElements = makeDexElements.invoke(null, fileList, optimizedDirectory, suppressedExceptions);
        return (Object[]) patchDexElements;
    }
}
