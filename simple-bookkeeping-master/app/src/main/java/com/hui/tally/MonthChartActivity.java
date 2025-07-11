package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hui.tally.adapter.ChartVPAdapter;
import com.hui.tally.db.DBManager;
import com.hui.tally.frag_chart.IncomChartFragment;
import com.hui.tally.frag_chart.OutcomChartFragment;
import com.hui.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// 月度图表Activity（显示月度收支统计和图表）
public class MonthChartActivity extends AppCompatActivity {
    // 收入和支出按钮
    Button inBtn, outBtn;
    // 日期、收入和支出统计文本
    TextView dateTv, inTv, outTv;
    // 图表视图容器
    ViewPager chartVp;

    // 当前显示的年份和月份
    int year;
    int month;

    // 日历对话框选择的位置和月份
    int selectPos = -1, selectMonth = -1;

    // Fragment列表
    List<Fragment> chartFragList;
    // 收入图表Fragment
    private IncomChartFragment incomChartFragment;
    // 支出图表Fragment
    private OutcomChartFragment outcomChartFragment;
    // 图表适配器
    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.activity_month_chart);
        // 初始化视图
        initView();
        // 初始化时间
        initTime();
        // 初始化统计数据
        initStatistics(year, month);
        // 初始化Fragment
        initFrag();
        // 设置ViewPager选择监听
        setVPSelectListener();
    }

    // 设置ViewPager页面选择监听器
    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 根据选中的页面设置按钮样式
                setButtonStyle(position);
            }
        });
    }

    // 初始化Fragment
    private void initFrag() {
        chartFragList = new ArrayList<>();
        // 创建收入和支出Fragment实例
        incomChartFragment = new IncomChartFragment();
        outcomChartFragment = new OutcomChartFragment();

        // 创建Bundle传递数据
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        // 设置参数到Fragment
        incomChartFragment.setArguments(bundle);
        outcomChartFragment.setArguments(bundle);

        // 将Fragment添加到列表（支出在前，收入在后）
        chartFragList.add(outcomChartFragment);
        chartFragList.add(incomChartFragment);

        // 创建并设置适配器
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVPAdapter);
    }

    /* 统计指定年月的收支情况 */
    private void initStatistics(int year, int month) {
        // 从数据库获取统计数据
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);  // 月收入总额
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0); // 月支出总额
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);  // 收入笔数
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0); // 支出笔数

        // 更新UI显示
        dateTv.setText(year + "年" + month + "月账单");
        inTv.setText("共" + incountItemOneMonth + "笔收入, ￥ " + inMoneyOneMonth);
        outTv.setText("共" + outcountItemOneMonth + "笔支出, ￥ " + outMoneyOneMonth);
    }

    /** 初始化时间为当前年月 */
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;  // 月份需要+1（0-based）
    }

    /** 初始化视图控件 */
    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);      // 收入按钮
        outBtn = findViewById(R.id.chart_btn_out);    // 支出按钮
        dateTv = findViewById(R.id.chart_tv_date);    // 日期文本
        inTv = findViewById(R.id.chart_tv_in);        // 收入统计文本
        outTv = findViewById(R.id.chart_tv_out);      // 支出统计文本
        chartVp = findViewById(R.id.chart_vp);        // 图表容器
    }

    // 点击事件处理
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:  // 返回按钮
                finish();  // 结束当前Activity
                break;
            case R.id.chart_iv_rili:  // 日历按钮
                showCalendarDialog();  // 显示日历选择对话框
                break;
            case R.id.chart_btn_in:   // 收入按钮
                setButtonStyle(1);    // 设置收入按钮样式
                chartVp.setCurrentItem(1);  // 切换到收入图表
                break;
            case R.id.chart_btn_out:  // 支出按钮
                setButtonStyle(0);     // 设置支出按钮样式
                chartVp.setCurrentItem(0);  // 切换到支出图表
                break;
        }
    }

    /* 显示日历选择对话框 */
    private void showCalendarDialog() {
        // 创建日历对话框
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();  // 显示对话框
        dialog.setDialogSize();  // 设置对话框尺寸

        // 设置日期选择回调
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                // 更新选择的位置和月份
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                // 刷新统计数据
                initStatistics(year, month);
                // 更新两个Fragment的日期
                incomChartFragment.setDate(year, month);
                outcomChartFragment.setDate(year, month);
            }
        });
    }

    /* 设置按钮样式（0-支出，1-收入） */
    private void setButtonStyle(int kind) {
        if (kind == 0) {  // 支出按钮选中
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        } else if (kind == 1) {  // 收入按钮选中
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}