package com.jf.router.api;

import android.content.Context;

import com.jf.commlib.log.LogW;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;

public class ClassUtils {

    public static Set<String> getClassesFromPackgeName(Context context,String packgeName){
        long startTime = System.currentTimeMillis();
        Set<String> set = new HashSet<>();
        try {
            //apk的dex文件，获得源码来反射
            DexFile dex = new DexFile(context.getPackageCodePath());
            //枚举类，来遍历所有源代码
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()){
                String className = entries.nextElement();
                LogW.d("ClassUtils","DexFile find class:"+className);
                if(className.contains(packgeName)){
                    //LogW.d("ClassUtils","DexFile find class:"+className);
                    set.add(className);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        LogW.d("ClassUtils","dex search coast time:"+(System.currentTimeMillis() - startTime));
        return set;
    }

    /**
     * 支持multidex查找
     * */
    public static Set<String> getClassesFromMultedex(Context context,String packgeName){
        // Here we do some reflection to access the dex files from the class loader. These implementation details vary by platform version,
        // so we have to be a little careful, but not a huge deal since this is just for testing. It should work on 21+.
        // The source for reference is at:
        // https://android.googlesource.com/platform/libcore/+/oreo-release/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
        //获取BaseDexClassLoader
        BaseDexClassLoader classLoader = (BaseDexClassLoader)context.getClassLoader();
        //反射获取BaseDexClassLoader内的pathList
        Field pathListField = reflectField("dalvik.system.BaseDexClassLoader", "pathList");
        //初始化集合
        Set<String> set = new HashSet<>();
        try {
            // Type is DexPathList
            Object pathList = pathListField.get(classLoader);
            //反射dexElements
            Field dexElementsField = reflectField("dalvik.system.DexPathList", "dexElements");
            // Type is Array<DexPathList.Element>
            Object[] dexElements = (Object[]) dexElementsField.get(pathList);
            //反射获取DexPathList内的Element内部类中dexFile[DexFile类型]
            Field dexFileField = reflectField("dalvik.system.DexPathList$Element", "dexFile");
            //遍历获取dexFiled
            for(Object item:dexElements){
                DexFile childDexFile = (DexFile) dexFileField.get(item);
                set.addAll(getChildDexFileClasses(childDexFile,packgeName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    private static Field reflectField(String className, String fieldName) {
        try {
            Class clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Set<String> getChildDexFileClasses(DexFile childDexFile,String packgeName){
        Set<String> set = new HashSet<>();
        try {
            //枚举类，来遍历所有源代码
            Enumeration<String> entries = childDexFile.entries();
            while (entries.hasMoreElements()){
                String className = entries.nextElement();
                //LogW.d("ClassUtils","DexFile find class:"+className);
                if(className.contains(packgeName)){
                    //LogW.d("ClassUtils","DexFile find class:"+className);
                    set.add(className);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return set;
    }
}
