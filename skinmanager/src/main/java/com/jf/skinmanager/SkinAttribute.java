package com.jf.skinmanager;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
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
        Resources res = view.getContext().getResources();
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
                    String resourceName;
                    try {
                        resourceName = res.getResourceEntryName(resId);
                    } catch (Resources.NotFoundException e) {
                        resourceName = "0x" + Integer.toHexString(resId);
                    }

                    TypedValue typedValue = new TypedValue();
                    view.getContext().getTheme().resolveAttribute(resId,typedValue,true);
                    //TypedArray a = view.getContext().obtainStyledAttributes(resId,R.styleable.AppCompatTheme);
                    //int realId = a.getResourceId(a.getIndex(0),-1);
                    LogW.d("lookView","value:" + value + " resourceName=>" + resourceName + " typedValue:" + typedValue.resourceId);
                    //a.recycle();
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
                LogW.e("applySkin","error: view is null");
                return;
            }
            for (SkinAttr attr:attrList) {
                switch (attr.name){
                    case "background":
//                        Object background = SkinResource.getInstance().getBackground(attr.value);
//                    if(background instanceof Drawable){
//                        view.setBackground((Drawable) background);
//                        LogW.d("applySkin","setBackground: from " + attr.value+" to "+ background);
//                    }else{
//                        view.setBackgroundColor((Integer) background);
//                        LogW.d("applySkin","setBackgroundColor: from " + attr.value+" to "+ background);
//                    }
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
                                ((TextView) view).setCompoundDrawables(null,drawable,null, null);
                                LogW.d("applySkin","drawableTop: "+ SkinResource.getInstance().getDrawable(attr.value));
                            }else{
                                LogW.e("error: drawableTop is null");
                            }
                        }
                        break;
                    case "drawableBottom":
                        if(view instanceof TextView){
                            Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                            if(drawable != null){
                                ((TextView) view).setCompoundDrawables(null,null,null, drawable);
                                LogW.d("applySkin","drawableBottom: "+ SkinResource.getInstance().getDrawable(attr.value));
                            }else{
                                LogW.e("error: drawableBottom is null");
                            }
                        }
                        break;
                    case "drawableLeft":
                        if(view instanceof TextView){
                            Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                            if(drawable != null){
                                ((TextView) view).setCompoundDrawables(drawable,null,null, null);
                                LogW.d("applySkin","drawableLeft: "+ SkinResource.getInstance().getDrawable(attr.value));
                            }else{
                                LogW.e("error: drawableLeft is null");
                            }
                        }
                        break;
                    case "drawableRight":
                        if(view instanceof TextView){
                            Drawable drawable = SkinResource.getInstance().getDrawable(attr.value);
                            if(drawable != null){
                                ((TextView) view).setCompoundDrawables(null,null,drawable, null);
                                LogW.d("applySkin","drawableRight: "+ SkinResource.getInstance().getDrawable(attr.value));
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
