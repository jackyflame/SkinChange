package com.jf.skinchange.ui.game;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.jf.commlib.log.LogW;
import com.jf.skinchange.R;
import com.jf.skinmanager.SkinManager;

public class GameViewModel extends ViewModel {

    public MutableLiveData<String> name = new MutableLiveData<>();
    public LiveData<String> nameFromat = Transformations.map(name, new Function<String, String>() {
        @Override
        public String apply(String input) {
            return input + " format!";
        }
    });
    public LiveData<String> nameFromat2 = Transformations.switchMap(name, new Function<String, LiveData<String>>() {
        @Override
        public LiveData<String> apply(String input) {
            MutableLiveData<String> newData = new MutableLiveData<>();
            newData.setValue(input + " format2!");
            return newData;
        }
    });

    private int clickCount = 0;

    public GameViewModel() {
        Log.d("ViewModel","GameViewModel create!!!");
    }

//    public void setName(String name) {
//        this.name.setValue(name);
//    }
//
//    public MutableLiveData<String> getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark.setValue(remark);
//    }

    public void onTextClick(View view){
        name.setValue("name click" + (clickCount++));
    }

    public void onBtnClick(View view){
        if(view.getId() == R.id.btn_skin_reset){
            LogW.d("=========>>>> 重置");
            SkinManager.getInstantce().reset();
        }else{
            LogW.d("=========>>>> 换肤");
            String pathName = Environment.getExternalStoragePublicDirectory("") + "/SkinManager/skiner.sk";
            SkinManager.getInstantce().loadSkin(pathName);
        }
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");//无类型限制
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        mActivity.startActivityForResult(intent, 1);
    }
}
