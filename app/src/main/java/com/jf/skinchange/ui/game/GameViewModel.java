package com.jf.skinchange.ui.game;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.view.View;

public class GameViewModel extends ViewModel {

    MutableLiveData<String> liveData = new MutableLiveData<>();
    private int clickCount = 0;

    public GameViewModel() {
        Log.d("ViewModel","GameViewModel create!!!");
    }

    public MutableLiveData<String> getName(){
        return liveData;
    }

    public void setName(String name) {
        liveData.setValue(name);
    }

    public void onTextClick(View view){
        setName("name click" + (clickCount++));
    }
}
