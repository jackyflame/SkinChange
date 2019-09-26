package com.jf.skinchange.ui.game;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

import com.jf.commlib.log.LogW;
import com.jf.skinchange.R;
import com.jf.skinchange.databinding.ActivityGameBinding;
import com.jf.skinchange.livedata.LiveDataBus_Ver1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class GameActivity extends AppCompatActivity {

    private GameViewModel viewModel;
    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initSDDir();
        viewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_game);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel.name.setValue("init");

        viewModel.nameFromat2.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                LogW.d("nameFromat2"," value change:"+s);
            }
        });

        //同样根据标签“bus1”获取发送过来的消息，这个消息是可以跨页面发送的，所以基本上已经跟我们平日里使用的eventbus相同了
        LiveDataBus_Ver1.get()
                .with("bus1",String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String msg) {
                        LogW.d("LiveDataBus_Ver1"," msg received:"+msg);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path;
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
                Toast.makeText(this,"path:" + path,Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                //path = getPath(this, uri);
                //Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initSDDir(){
        String sdStatus = Environment.getExternalStorageState();
        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            LogW.d("initSDDir", "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            String pathName = Environment.getExternalStoragePublicDirectory("") + "/SkinManager";
            File path = new File(pathName);
            if( !path.exists()) {
                LogW.d("TestFile", "Create the path:" + pathName);
                path.mkdirs();
            }
            String fileName="file-test.txt";
            FileWriter fw = new FileWriter(path +"/"+ fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            String s = "this is a test string writing to file.";
            bw.write(s);
            bw.close();
            fw.close();
            LogW.d("TestFile", "write to file:" + s);
        } catch(Exception e) {
            LogW.e("TestFile", "Error on writeFilToSD.");
            e.printStackTrace();
        }
    }
}
