package com.jf.skinchange.target;

public class InAsmTarget {

    public static void main(String[] args){
        new InAsmTarget().test2();
    }

    public void test() throws InterruptedException {
        Thread.sleep(1000);
    }

    @AMSTest
    public void test2() {
        System.out.println("test2 go go go !");
    }

}
