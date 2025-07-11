package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置Activity的布局文件（activity_about.xml）
        setContentView(R.layout.activity_about);
    }

    // 统一的点击事件处理方法（用于关闭页面）
    public void onClick(View view) {
        // 结束当前Activity，返回上一页面
        finish();
    }
}