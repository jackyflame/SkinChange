package com.jf.skinmanager;

import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SkinAttribute {

    private static final Set<String> mAttrsFilter = new HashSet<>();
    static{
        mAttrsFilter.add("background");
        mAttrsFilter.add("src");
        mAttrsFilter.add("textColor");
        mAttrsFilter.add("drawableTop");
        mAttrsFilter.add("drawableBottom");
        mAttrsFilter.add("drawableLeft");
        mAttrsFilter.add("drawableRight");
    }

    private List<SkinAttrView> skinAttrViewList = new ArrayList<>();

    public void lookView(View view, AttributeSet attrs){
        SkinAttrView skinAttrView = new SkinAttrView(view);
        for(int i=0;i<attrs.getAttributeCount();i++){
            if(mAttrsFilter.contains(attrs.getAttributeName(i))){
                int value = attrs.getAttributeIntValue(i,-404);
                if(value != -404){
                    skinAttrView.addAttr(attrs.getAttributeName(i),value);
                }
            }
        }
        skinAttrViewList.add(skinAttrView);
    }

    public void applySkin(){
        for (SkinAttrView view:skinAttrViewList) {
            view.applySkin();
        }
    }

    class SkinAttrView{

        View view = null;
        List<SkinAttr> attrList = new ArrayList<>();

        public SkinAttrView(View view) {
            this.view = view;
        }

        public void addAttr(String name,int value){
            this.attrList.add(new SkinAttr(name,value));
        }

        public void applySkin(){
            if(view == null){
                return;
            }
            for (SkinAttr attr:attrList) {
                //view.setBackground();
            }
        }
    }

    class SkinAttr{

        String name;
        int value;

        public SkinAttr(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

}
