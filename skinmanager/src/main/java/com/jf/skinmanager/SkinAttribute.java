package com.jf.skinmanager;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
        if(view == null){
            LogW.e("lookView","error: view is null!!!");
            return;
        }
        Resources res = view.getContext().getResources();
        //LogW.d("lookView","view:" + view.getId());

        SkinAttrView skinAttrView = new SkinAttrView(view);

        //TypedArray typedArray = view.getContext().obtainStyledAttributes(attrs);
        //TypedArray arr = view.getContext().obtainStyledAttributes(com.android.internal.R.styleable.View);
        //final TypedArray a = view.getContext().obtainStyledAttributes(
        //        attrs, com.android.internal.R.styleable.View, 0, 0);

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
                    resId = Integer.parseInt(value.substring(1));
                    //String resourceName;
                    //try {
                    //    resourceName = res.getResourceName(attrId);
                    //} catch (Resources.NotFoundException e) {
                    //    resourceName = "0x" + Integer.toHexString(resId);
                    //}
                    //TypedArray a = view.getContext().obtainStyledAttributes(attrs,R.styleable.View);
                    //int realId = a.getResourceId(a.getIndex(0),-1);
                    //LogW.d("lookView","value:" + value + " resourceName=>"+resourceName + " realId:"+realId);
                    //a.recycle();
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
        LogW.d("----------applySkin-------------");
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
                if("background".equals(attr.name)){
                    view.setBackgroundResource(SkinResource.getInstance().getSkinIdentify(attr.value));
                }else if("textColor".equals(attr.name)){
                    if(view instanceof TextView){
                        ((TextView) view).setTextColor(SkinResource.getInstance().getColor(attr.value));
                    }
                }else if("src".equals(attr.name)){
                    if(view instanceof ImageView){
                        ((ImageView) view).setImageResource(attr.value);
                    }
                }else if("drawableTop".equals(attr.name)){
                    if(view instanceof TextView){
                        Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                        if(drawable != null){
                            ((TextView) view).setCompoundDrawables(null,drawable,null, null);
                        }else{
                            LogW.e("error: drawableTop is null");
                        }
                    }
                }else if("drawableBottom".equals(attr.name)){
                    if(view instanceof TextView){
                        Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                        if(drawable != null){
                            ((TextView) view).setCompoundDrawables(null,null,null, drawable);
                        }else{
                            LogW.e("error: drawableBottom is null");
                        }
                    }
                }else if("drawableLeft".equals(attr.name)){
                    if(view instanceof TextView){
                        Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                        if(drawable != null){
                            ((TextView) view).setCompoundDrawables(drawable,null,null, null);
                        }else{
                            LogW.e("error: drawableLeft is null");
                        }
                    }
                }else if("drawableRight".equals(attr.name)){
                    if(view instanceof TextView){
                        Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                        if(drawable != null){
                            ((TextView) view).setCompoundDrawables(null,null,drawable, null);
                        }else{
                            LogW.e("error: drawableRight is null");
                        }
                    }
                }
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
