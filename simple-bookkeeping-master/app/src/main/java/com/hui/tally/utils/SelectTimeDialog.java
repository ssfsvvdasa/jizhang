package com.hui.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hui.tally.R;

/*
 * 在记录页面弹出时间对话框
 * */
public class SelectTimeDialog extends Dialog implements View.OnClickListener {
    // 输入小时和分钟的编辑框
    EditText hourEt, minuteEt;
    // 日期选择器
    DatePicker datePicker;
    // 确定和取消按钮
    Button ensureBtn, cancelBtn;

    // 确定按钮回调接口
    public interface OnEnsureListener {
        public void onEnsure(String time, int year, int month, int day);
    }

    OnEnsureListener onEnsureListener;

    // 设置回调监听器
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    // 构造函数
    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框布局
        setContentView(R.layout.dialog_time);
        // 初始化视图组件
        hourEt = findViewById(R.id.dialog_time_et_hour);
        minuteEt = findViewById(R.id.dialog_time_et_minute);
        datePicker = findViewById(R.id.dialog_time_dp);
        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_time_btn_cancel);
        // 设置按钮点击监听
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        // 隐藏DatePicker的头部（年/月/日标题）
        hideDatePickerHeader();
    }

    // 按钮点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_time_btn_cancel:  // 取消按钮
                cancel();  // 关闭对话框
                break;
            case R.id.dialog_time_btn_ensure:  // 确定按钮
                // 获取选择的日期
                int year = datePicker.getYear();  // 选择年份
                int month = datePicker.getMonth() + 1;  // 月份（0-based，需+1）
                int dayOfMonth = datePicker.getDayOfMonth();  // 日

                // 格式化月份（补零）
                String monthStr = String.valueOf(month);
                if (month < 10) {
                    monthStr = "0" + month;
                }
                // 格式化日期（补零）
                String dayStr = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    dayStr = "0" + dayOfMonth;
                }

                // 获取输入的小时和分钟
                String hourStr = hourEt.getText().toString();
                String minuteStr = minuteEt.getText().toString();

                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour = hour % 24;  // 处理24小时制
                }

                int minute = 0;
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr);
                    minute = minute % 60;  // 处理60分钟制
                }

                // 格式化小时和分钟（补零）
                hourStr = String.valueOf(hour);
                minuteStr = String.valueOf(minute);
                if (hour < 10) {
                    hourStr = "0" + hour;
                }
                if (minute < 10) {
                    minuteStr = "0" + minute;
                }

                // 构建完整的时间字符串
                String timeFormat = year + "年" + monthStr + "月" + dayStr + "日 " + hourStr + ":" + minuteStr;

                // 触发回调
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure(timeFormat, year, month, dayOfMonth);
                }
                cancel();  // 关闭对话框
                break;
        }
    }

    // 隐藏DatePicker头布局（年/月/日标题）
    private void hideDatePickerHeader() {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }

        // 5.0+系统处理方式
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);  // 隐藏头部
            // 调整布局参数
            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);
            return;
        }

        // 6.0+系统处理方式
        headerId = getContext().getResources().getIdentifier("date_picker_header", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);  // 隐藏头部
        }
    }
}