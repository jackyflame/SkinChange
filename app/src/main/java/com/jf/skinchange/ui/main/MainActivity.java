package com.jf.skinchange.ui.main;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jf.skinchange.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView txv_msg = findViewById(R.id.txv_msg);
        final MediatorLiveData<String> test = new MediatorLiveData<>();

        /*--------------------重点在这里--------------------*/
        test.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d("LiveData","msg:"+s);
                txv_msg.setText(s);
            }
        });
        /*-----------------------------------------------*/

        //模拟正常调用
        test.setValue("test");
        //模拟调用
        txv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txv_msg.post(new Runnable() {
                    @Override
                    public void run() {
                        test.postValue("go go go ");
                    }
                });
            }
        });
    }
}
