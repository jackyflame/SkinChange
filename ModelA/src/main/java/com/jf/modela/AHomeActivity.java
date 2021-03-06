package com.jf.modela;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.jf.router.annotation.Router;
import com.jf.router.api.RouterManager;

@Router("/a/home")
public class AHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn = findViewById(R.id.btn_jump);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(AHomeActivity.this,BHomeActivity.class);
                RouterManager.getInstance().jump("/b/home");
            }
        });
    }

    @Router("/a/home/inner")
    public class AInnerActivity {

    }
}
