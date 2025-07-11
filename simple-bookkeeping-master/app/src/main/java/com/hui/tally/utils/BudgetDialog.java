package com.hui.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;  // 导入文本工具类
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;  // 导入Toast提示工具

import androidx.annotation.NonNull;

import com.hui.tally.R;

// 预算设置对话框类
public class BudgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIv;      // 取消图标
    Button ensureBtn;        // 确定按钮
    EditText moneyEt;        // 金额输入框

    // 确定按钮回调接口
    public interface OnEnsureListener {
        public void onEnsure(float money);
    }

    OnEnsureListener onEnsureListener;  // 回调接口实例

    // 设置回调监听器
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    // 构造函数
    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框布局
        setContentView(R.layout.dialog_budget);
        // 初始化视图组件
        cancelIv = findViewById(R.id.dialog_budget_iv_error);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_budget_et);
        // 设置点击监听
        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    // 点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_budget_iv_error:
                cancel();  // 点击取消图标关闭对话框
                break;
            case R.id.dialog_budget_btn_ensure:
                // 获取输入金额
                String data = moneyEt.getText().toString();
                // 检查输入是否为空
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(), "输入数据不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 转换为浮点数
                float money = Float.parseFloat(data);
                // 检查金额是否有效
                if (money <= 0) {
                    Toast.makeText(getContext(), "预算金额必须大于0", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 触发回调
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure(money);
                }
                cancel();  // 关闭对话框
                break;
        }
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
        // 延时100毫秒弹出键盘
        handler.sendEmptyMessageDelayed(1, 100);
    }

    // 创建Handler处理键盘弹出
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 获取输入法服务
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            // 切换软键盘显示状态
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}