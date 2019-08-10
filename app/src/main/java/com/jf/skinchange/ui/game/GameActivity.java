package com.jf.skinchange.ui.game;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jf.skinchange.R;
import com.jf.skinchange.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private GameViewModel viewModel;
    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_game);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        //setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel.setName("init");
    }

}
