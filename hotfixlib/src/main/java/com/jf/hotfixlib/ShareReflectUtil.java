package com.jf.hotfixlib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ShareReflectUtil {

    public static Field getFiled(Object instance,String name) throws NoSuchFieldException {
        for(Class<?> cls = instance.getClass();cls != null; cls = cls.getSuperclass()){
            try {
                Field field = cls.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {}
        }
        throw new NoSuchFieldException("Field["+name+"] not found in "+instance.getClass());
    }

    public static Method getMethod(Object instance, String name) throws NoSuchMethodException {
        for(Class<?> cls = instance.getClass();cls != null; cls = cls.getSuperclass()){
            try {
                Method method = cls.getDeclaredMethod(name,cls);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {}
        }
        throw new NoSuchMethodException("Method["+name+"] not found in "+instance.getClass());
    }

}
