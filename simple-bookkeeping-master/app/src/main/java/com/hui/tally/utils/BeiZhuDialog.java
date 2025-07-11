package com.hui.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hui.tally.R;

// 自定义备注对话框类
public class BeiZhuDialog extends Dialog implements View.OnClickListener {
    EditText et;        // 备注输入框
    Button cancelBtn;   // 取消按钮
    Button ensureBtn;   // 确定按钮
    OnEnsureListener onEnsureListener;  // 确定按钮回调接口

    // 设置确定按钮回调接口的方法
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    // 构造函数
    public BeiZhuDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框布局
        setContentView(R.layout.dialog_beizhu);
        // 初始化布局控件
        et = findViewById(R.id.dialog_beizhu_et);
        cancelBtn = findViewById(R.id.dialog_beizhu_btn_cancel);
        ensureBtn = findViewById(R.id.dialog_beizhu_btn_ensure);
        // 设置按钮点击监听
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    // 确定按钮回调接口定义
    public interface OnEnsureListener{
        public void onEnsure();
    }

    // 按钮点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_beizhu_btn_cancel:
                cancel();  // 取消对话框
                break;
            case R.id.dialog_beizhu_btn_ensure:
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure();  // 触发确定回调
                }
                break;
        }
    }

    // 获取输入框内容的方法
    public String getEditText(){
        // 返回去除空格的输入文本
        return et.getText().toString().trim();
    }

    /* 设置对话框尺寸与屏幕匹配 */
    public void setDialogSize(){
        // 获取当前窗口对象
        Window window = getWindow();
        // 获取窗口参数对象
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕尺寸
        Display d = window.getWindowManager().getDefaultDisplay();
        // 设置对话框宽度为屏幕宽度
        wlp.width = (int)(d.getWidth());
        // 设置对话框位置在屏幕底部
        wlp.gravity = Gravity.BOTTOM;
        // 设置对话框背景透明
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 应用参数设置
        window.setAttributes(wlp);
        // 延时100毫秒发送消息（用于弹出键盘）
        handler.sendEmptyMessageDelayed(1, 100);
    }

    // 创建Handler处理消息
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 获取输入法管理器
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            // 切换软键盘显示状态（0表示无标志，HIDE_NOT_ALWAYS表示即使之前显示也不强制隐藏）
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}