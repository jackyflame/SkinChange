package com.jf.skinmanager;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.jf.commlib.log.LogW;

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
            String attrName = attrs.getAttributeName(i);
            if(mAttrsFilter.contains(attrName)){
                String value = attrs.getAttributeValue(i);
                //颜色以#开头，写死的，不能替换
                if(value.startsWith("#")){
                    continue;
                }
                int resId = -1;
                //？开头的表示使用属性
                if(value.startsWith("?")){
                    int attrId = Integer.parseInt(value.substring(1));
                    TypedArray set = view.getContext().obtainStyledAttributes(new int[]{attrId});
                    set.getResourceId(0,-1);
                    //resId = SkinThemeUtils.getResId(view.getContext(),new int[]{attrId})[0];
                }else{
                    //正常以@开头的
                    resId = Integer.parseInt(value.substring(1));
                }
                LogW.d("SkinAttribute"," attName:"+attrName+" = " + value);
                skinAttrView.addAttr(attrName,resId);
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
