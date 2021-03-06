package com.jf.skinmanager;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
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
                    resId = Integer.parseInt(value.substring(1));
                    TypedValue typedValue = new TypedValue();
                    view.getContext().getTheme().resolveAttribute(resId,typedValue,true);
                    resId = typedValue.resourceId;
                }else{
                    //正常以@开头的
                    resId = Integer.parseInt(value.substring(1));
                }
                LogW.d("SkinAttribute"," attName:"+attrName+" = " + value);
                skinAttrView.addAttr(attrName,resId);
            }
        }
        skinAttrViewList.add(skinAttrView);

        if(!SkinResource.getInstance().isDefaultSkin()){
            skinAttrView.applySkin();
        }
    }

    public void applySkin(){
        if(!SkinResource.getInstance().isSkinResExits()){
            LogW.e("applySkin","error: skinRes is not exits");
            return;
        }
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
                LogW.e("applySkin","error: view is null");
                return;
            }
            if(!SkinResource.getInstance().isSkinResExits()){
                LogW.e("applySkin","error: skinRes is not exits");
                return;
            }
            for (SkinAttr attr:attrList) {
                switch (attr.name){
                    case "background":
                        Object background = SkinResource.getInstance().getBackground(attr.value);
                        if(background instanceof Drawable){
                            view.setBackground((Drawable) background);
                            LogW.d("applySkin","setBackground: from " + attr.value+" to "+ background);
                        }else{
                            view.setBackgroundColor((Integer) background);
                            LogW.d("applySkin","setBackgroundColor: from " + attr.value+" to "+ background);
                        }
                        break;
                    case "textColor":
                        if(view instanceof TextView){
                            ColorStateList newColor = SkinResource.getInstance().getColorStateList(attr.value);
                            ((TextView) view).setTextColor(newColor);
                            LogW.d("applySkin","textColor: from "+Integer.toHexString(attr.value)+" to "+ newColor);
                        }
                        break;
                    case "src":
                        if(view instanceof ImageView){
                            ((ImageView) view).setImageResource(SkinResource.getInstance().getSkinIdentify(attr.value));
                            LogW.d("applySkin","setImageResource: "+ SkinResource.getInstance().getSkinIdentify(attr.value));
                        }
                        break;
                    case "drawableTop":
                        if(view instanceof TextView){
                            Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                            if(drawable != null){
                                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(null,drawable,null, null);
                                LogW.d("applySkin","drawableTop: "+ drawable);
                            }else{
                                LogW.e("error: drawableTop is null");
                            }
                        }
                        break;
                    case "drawableBottom":
                        if(view instanceof TextView){
                            Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                            if(drawable != null){
                                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(null,null,null, drawable);
                                LogW.d("applySkin","drawableBottom: "+ drawable);
                            }else{
                                LogW.e("error: drawableBottom is null");
                            }
                        }
                        break;
                    case "drawableLeft":
                        if(view instanceof TextView){
                            Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                            if(drawable != null){
                                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(drawable,null,null, null);
                                LogW.d("applySkin","drawableLeft: "+ drawable);
                            }else{
                                LogW.e("error: drawableLeft is null");
                            }
                        }
                        break;
                    case "drawableRight":
                        if(view instanceof TextView){
                            Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                            if(drawable != null){
                                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(null,null,drawable, null);
                                LogW.d("applySkin","drawableRight: "+ drawable);
                            }else{
                                LogW.e("error: drawableRight is null");
                            }
                        }
                        break;
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
