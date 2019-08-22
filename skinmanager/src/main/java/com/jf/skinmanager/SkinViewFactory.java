package com.jf.skinmanager;

import android.content.Context;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SkinViewFactory implements LayoutInflater.Factory2, Observer {

    private final Object[] mConstructorArgs = new Object[2];
    private Context mContext;
    private LayoutInflater.Filter mFilter;
    private HashMap<String, Boolean> mFilterMap;
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<String, Constructor<? extends View>>();
    private static final Class<?>[] mConstructorSignature = new Class[] {Context.class, AttributeSet.class};
    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];

    private SkinAttribute skinAttribute = new SkinAttribute();

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return onCreateView(name,context,attrs);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        this.mContext = context;

        mFilter = LayoutInflater.from(context).getFilter();
        if (mFilter != null) {
            mFilterMap = new HashMap<>();
        }

        View view = null;
        final Object lastContext = mConstructorArgs[0];
        mConstructorArgs[0] = context;
        try {
            if (-1 == name.indexOf('.')) {
                view = createSdkView(name, attrs);
            } else {
                view = createView(name, null, attrs);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            mConstructorArgs[0] = lastContext;
        }
        return view;
    }

    private View createSdkView(String name, AttributeSet attrs) throws ClassNotFoundException {
        return createView(name,"android.view.", attrs);
    }

    private View createView(String name, String prefix, AttributeSet attrs) throws ClassNotFoundException{
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor != null && !verifyClassLoader(constructor)) {
            constructor = null;
            sConstructorMap.remove(name);
        }
        Class<? extends View> clazz = null;
        String finalPrefix = prefix != null ? (prefix + name) : name;
        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                clazz = mContext.getClassLoader().loadClass(finalPrefix).asSubclass(View.class);
                if (mFilter != null && clazz != null) {
                    boolean allowed = mFilter.onLoadClass(clazz);
                    if (!allowed) {
                        failNotAllowed(name, prefix, attrs);
                    }
                }
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            } else {
                // If we have a filter, apply it to cached constructor
                if (mFilter != null) {
                    // Have we seen this name before?
                    Boolean allowedState = mFilterMap.get(name);
                    if (allowedState == null) {
                        // New class -- remember whether it is allowed
                        clazz = mContext.getClassLoader().loadClass(finalPrefix).asSubclass(View.class);
                        boolean allowed = clazz != null && mFilter.onLoadClass(clazz);
                        mFilterMap.put(name, allowed);
                        if (!allowed) {
                            failNotAllowed(name, prefix, attrs);
                        }
                    } else if (allowedState.equals(Boolean.FALSE)) {
                        failNotAllowed(name, prefix, attrs);
                    }
                }
            }

            Object lastContext = mConstructorArgs[0];
            if (mConstructorArgs[0] == null) {
                // Fill in the context if not already within inflation.
                mConstructorArgs[0] = mContext;
            }
            Object[] args = mConstructorArgs;
            args[1] = attrs;

            final View view = constructor.newInstance(args);
            mConstructorArgs[0] = lastContext;

            //记录view 参数
            skinAttribute.lookView(view,attrs);

            return view;

        } catch (NoSuchMethodException e) {
            final InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + finalPrefix, e);
            ie.setStackTrace(EMPTY_STACK_TRACE);
            throw ie;

        } catch (ClassCastException e) {
            // If loaded class is not a View subclass
            final InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Class is not a View " + finalPrefix, e);
            ie.setStackTrace(EMPTY_STACK_TRACE);
            throw ie;
        } catch (ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            throw e;
        } catch (Exception e) {
            final InflateException ie = new InflateException(
                    attrs.getPositionDescription() + ": Error inflating class "
                            + (clazz == null ? "<unknown>" : clazz.getName()), e);
            ie.setStackTrace(EMPTY_STACK_TRACE);
            throw ie;
        }
    }

    private static final ClassLoader BOOT_CLASS_LOADER = LayoutInflater.class.getClassLoader();

    private final boolean verifyClassLoader(Constructor<? extends View> constructor) {
        final ClassLoader constructorLoader = constructor.getDeclaringClass().getClassLoader();
        if (constructorLoader == BOOT_CLASS_LOADER) {
            // fast path for boot class loader (most common case?) - always ok
            return true;
        }
        // in all normal cases (no dynamic code loading), we will exit the following loop on the
        // first iteration (i.e. when the declaring classloader is the contexts class loader).
        ClassLoader cl = mContext.getClassLoader();
        do {
            if (constructorLoader == cl) {
                return true;
            }
            cl = cl.getParent();
        } while (cl != null);
        return false;
    }

    /**
     * Throw an exception because the specified class is not allowed to be inflated.
     */
    private void failNotAllowed(String name, String prefix, AttributeSet attrs) {
        throw new InflateException(attrs.getPositionDescription()
                + ": Class not allowed to be inflated "+ (prefix != null ? (prefix + name) : name));
    }

    @Override
    public void update(Observable o, Object arg) {
        if(skinAttribute != null){
             skinAttribute.applySkin();
        }
    }
}
