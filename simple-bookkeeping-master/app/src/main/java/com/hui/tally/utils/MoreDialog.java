package com.hui.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.hui.tally.AboutActivity;
import com.hui.tally.HistoryActivity;
import com.hui.tally.MonthChartActivity;
import com.hui.tally.R;
import com.hui.tally.SettingActivity;

// 更多功能对话框类
public class MoreDialog extends Dialog implements View.OnClickListener {
    // 对话框中的按钮
    Button aboutBtn, settingBtn, historyBtn, infoBtn;
    // 关闭图标
    ImageView errorIv;

    // 构造函数
    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框布局
        setContentView(R.layout.dialog_more);

        // 初始化视图组件
        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        historyBtn = findViewById(R.id.dialog_more_btn_record);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        errorIv = findViewById(R.id.dialog_more_iv);

        // 设置按钮点击监听
        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        errorIv.setOnClickListener(this);
    }

    // 按钮点击事件处理
    @Override
    public void onClick(View v) {
        // 创建跳转意图
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.dialog_more_btn_about:  // "关于"按钮
                // 跳转到关于页面
                intent.setClass(getContext(), AboutActivity.class);
                getContext().startActivity(intent);
                break;

            case R.id.dialog_more_btn_setting:  // "设置"按钮
                // 跳转到设置页面
                intent.setClass(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
                break;

            case R.id.dialog_more_btn_record:  // "历史记录"按钮
                // 跳转到历史记录页面
                intent.setClass(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;

            case R.id.dialog_more_btn_info:  // "图表信息"按钮
                // 跳转到月度图表页面
                intent.setClass(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;

            case R.id.dialog_more_iv:  // 关闭图标
                // 无额外操作，只关闭对话框
                break;
        }
        cancel();  // 关闭对话框
    }

    /* 设置对话框尺寸与屏幕匹配 */
    public void setDialogSize() {
        // 获取窗口对象
        Window window = getWindow();
        // 获取布局参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕尺寸
        Display d = window.getWindowManager().getDefaultDisplay();
        // 设置对话框宽度为屏幕宽度
        wlp.width = (int) (d.getWidth());
        // 设置对话框位置在屏幕底部
        wlp.gravity = Gravity.BOTTOM;
        // 设置透明背景
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 应用参数设置
        window.setAttributes(wlp);
    }
}