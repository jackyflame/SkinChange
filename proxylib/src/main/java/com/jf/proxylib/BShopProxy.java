package com.jf.proxylib;

public class BShopProxy implements IShop {

    private IShop target;

    public BShopProxy(IShop target) {
        this.target = target;
    }

    @Override
    public void buy() {
        System.out.println("BShopProxy BUY running...");
        target.buy();
    }

}
