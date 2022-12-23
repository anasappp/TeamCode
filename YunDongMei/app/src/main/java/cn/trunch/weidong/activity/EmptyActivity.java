package cn.trunch.weidong.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import cn.trunch.weidong.R;
import cn.trunch.weidong.util.StatusBarUtil;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorLight);
        ImageView emptyExitBtn = findViewById(R.id.emptyExitBtn);
        emptyExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
