package com.jf.proxylib;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testProxy(){
        final AShop aShop = new AShop();

        //BShopProxy B = new BShopProxy(aShop);
        //B.buy();

        IShop proxy = (IShop) Proxy.newProxyInstance(aShop.getClass().getClassLoader(),
                aShop.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if("buy".equals(method.getName())){
                    System.out.println("动态代理>>");
                    return method.invoke(aShop,args);
                }
                return null;
            }
        });

        proxy.buy();
    }
}