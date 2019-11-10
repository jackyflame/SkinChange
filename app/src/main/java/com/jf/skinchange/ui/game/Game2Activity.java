package com.jf.skinchange.ui.game;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.jf.skinchange.R;
import com.jf.skinchange.databinding.ActivityGame2Binding;
import com.jf.skinchange.livedata.LiveDataBus_Ver1;

public class Game2Activity extends AppCompatActivity {

    private Game2ViewModel viewModel;
    private ActivityGame2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game2);
        viewModel = ViewModelProviders.of(this).get(Game2ViewModel.class);
        viewModel.setActivity(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_game2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        viewModel.name.setValue("12345555");

        LiveDataBus_Ver1.get()
                .with("bus1")
                .setValue("send msg 1");
    }
}
