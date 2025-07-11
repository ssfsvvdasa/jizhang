package com.hui.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hui.tally.R;
import com.hui.tally.adapter.CalendarAdapter;
import com.hui.tally.db.DBManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// 日历选择对话框类（用于选择年份和月份）
public class CalendarDialog extends Dialog implements View.OnClickListener {
    ImageView errorIv;       // 关闭对话框的图标
    GridView gv;            // 月份网格视图
    LinearLayout hsvLayout; // 年份水平布局容器

    List<TextView> hsvViewList; // 存储年份TextView的集合
    List<Integer> yearList;     // 存储可用年份的集合

    int selectPos = -1;    // 当前选中的年份位置
    private CalendarAdapter adapter; // 月份网格适配器
    int selectMonth = -1;  // 当前选中的月份

    // 刷新回调接口（当选择改变时通知外部）
    public interface OnRefreshListener {
        public void onRefresh(int selPos, int year, int month);
    }

    OnRefreshListener onRefreshListener; // 回调接口实例

    // 设置刷新监听器
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    // 构造函数（传入当前选中的年份位置和月份）
    public CalendarDialog(@NonNull Context context, int selectPos, int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框布局
        setContentView(R.layout.dialog_calendar);
        // 初始化视图组件
        gv = findViewById(R.id.dialog_calendar_gv);
        errorIv = findViewById(R.id.dialog_calendar_iv);
        hsvLayout = findViewById(R.id.dialog_calendar_layout);
        // 设置关闭图标点击事件
        errorIv.setOnClickListener(this);
        // 向年份布局添加视图
        addViewToLayout();
        // 初始化月份网格视图
        initGridView();
        // 设置网格项点击事件
        setGVListener();
    }

    // 设置月份网格点击监听
    private void setGVListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 更新选中位置并刷新网格
                adapter.selPos = position;
                adapter.notifyDataSetInvalidated();
                // 计算实际月份（位置+1）
                int month = position + 1;
                int year = adapter.year;
                // 触发回调通知外部
                onRefreshListener.onRefresh(selectPos, year, month);
                // 关闭对话框
                cancel();
            }
        });
    }

    // 初始化月份网格视图
    private void initGridView() {
        // 获取选中的年份
        int selYear = yearList.get(selectPos);
        // 创建月份适配器
        adapter = new CalendarAdapter(getContext(), selYear);
        // 设置默认选中月份
        if (selectMonth == -1) {
            // 如果未指定，使用当前月份
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selPos = month;
        } else {
            // 月份索引从0开始（传入值-1）
            adapter.selPos = selectMonth - 1;
        }
        // 设置适配器
        gv.setAdapter(adapter);
    }

    // 向年份布局添加视图
    private void addViewToLayout() {
        // 初始化集合
        hsvViewList = new ArrayList<>();
        // 从数据库获取可用年份列表
        yearList = DBManager.getYearListFromAccounttb();
        // 如果没有数据，添加当前年份
        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }

        // 遍历年份，为每个年份创建视图
        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            // 加载年份项布局
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv, null);
            // 添加到水平布局
            hsvLayout.addView(view);
            // 获取TextView并设置年份
            TextView hsvTv = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTv.setText(year + "");
            // 添加到管理集合
            hsvViewList.add(hsvTv);
        }

        // 设置默认选中年份（最近一年）
        if (selectPos == -1) {
            selectPos = hsvViewList.size() - 1;
        }
        // 更新选中状态显示
        changeTvbg(selectPos);
        // 设置年份点击监听
        setHSVClickListener();
    }

    /** 为年份文本设置点击监听 */
    private void setHSVClickListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView view = hsvViewList.get(i);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 更新选中状态显示
                    changeTvbg(pos);
                    // 记录选中位置
                    selectPos = pos;
                    // 获取选中年份
                    int year = yearList.get(selectPos);
                    // 更新月份网格显示
                    adapter.setYear(year);
                }
            });
        }
    }

    /** 更新选中年份的显示样式 */
    private void changeTvbg(int selectPos) {
        // 重置所有年份样式
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView tv = hsvViewList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_bg);
            tv.setTextColor(Color.BLACK);
        }

        // 设置选中年份样式
        TextView selView = hsvViewList.get(selectPos);
        selView.setBackgroundResource(R.drawable.main_recordbtn_bg);
        selView.setTextColor(Color.WHITE);
    }

    // 点击事件处理（关闭对话框）
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_calendar_iv:
                cancel();
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
        // 设置对话框位置在屏幕顶部
        wlp.gravity = Gravity.TOP;
        // 设置透明背景
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 应用参数设置
        window.setAttributes(wlp);
    }
}