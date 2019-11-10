package com.jf.skinchange.ui.game;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.jf.commlib.log.LogW;
import com.jf.skinchange.R;

public class Game2ViewModel extends ViewModel {

    private Game2Activity activity;
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<Integer> img = new MutableLiveData<>();
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
        img.setValue(R.drawable.ic_left);
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

    public int getImgRes(){
        return R.drawable.ic_left;
    }
}
