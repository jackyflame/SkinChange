package com.jf.skinchange.ui.game;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.jf.commlib.log.LogW;
import com.jf.skinmanager.SkinManager;

public class Game2ViewModel extends ViewModel {

    private Game2Activity activity;
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

    public Game2ViewModel() {
        Log.d("ViewModel","GameViewModel create!!!");
    }

    public String getTitle(){
        return "test!!!";
    }

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
        LogW.d("=========>>>> 跳转");
        activity.startActivity(new Intent(activity,GameActivity.class));
    }

    public void setActivity(Game2Activity game2Activity) {
        this.activity = game2Activity;
    }
}
