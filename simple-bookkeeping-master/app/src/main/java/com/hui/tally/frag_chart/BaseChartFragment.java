package com.hui.tally.frag_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import com.hui.tally.R;
import com.hui.tally.adapter.ChartItemAdapter;
import com.hui.tally.db.ChartItemBean;
import com.hui.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的图表基类Fragment
 */
abstract public class BaseChartFragment extends Fragment {
    ListView chartLv;     // 列表视图，用于显示收支明细
    int year;            // 当前选中的年份
    int month;           // 当前选中的月份
    List<ChartItemBean>mDatas;   // 数据源列表，存储图表项数据
    private ChartItemAdapter itemAdapter;  // 自定义列表适配器
    BarChart barChart;     // 柱状图控件
    TextView chartTv;      // 提示文本（无数据时显示）

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 加载布局文件
        View view =  inflater.inflate(R.layout.fragment_incom_chart, container, false);
        // 查找列表视图
        chartLv = view.findViewById(R.id.frag_chart_lv);
        // 获取从Activity传递过来的参数
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        // 初始化数据源
        mDatas = new ArrayList<>();
        // 创建适配器并绑定数据源
        itemAdapter = new ChartItemAdapter(getContext(), mDatas);
        // 为列表视图设置适配器
        chartLv.setAdapter(itemAdapter);
        // 添加列表头部视图（包含柱状图）
        addLVHeaderView();
        return view;
    }

    // 添加列表头部视图的方法
    protected  void addLVHeaderView(){
        // 加载头部布局文件
        View headerView = getLayoutInflater().inflate(R.layout.item_chartfrag_top,null);
        // 将头部视图添加到ListView
        chartLv.addHeaderView(headerView);
        // 获取头部布局中的控件
        barChart = headerView.findViewById(R.id.item_chartfrag_chart);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv);
        // 隐藏柱状图描述
        barChart.getDescription().setEnabled(false);
        // 设置柱状图内边距
        barChart.setExtraOffsets(20, 20, 20, 20);
        // 设置坐标轴
        setAxis(year,month);
        // 抽象方法：设置柱状图数据（由子类实现）
        setAxisData(year,month);
    }

    /** 抽象方法：设置柱状图数据（必须由子类实现）*/
    protected abstract void setAxisData(int year, int month);

    /** 设置柱状图坐标轴 */
    protected  void setAxis(int year, final int month){
        // 获取X轴对象
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴显示在底部
        xAxis.setDrawGridLines(true);  // 启用网格线绘制
        // 设置X轴标签数量（每月最多31天）
        xAxis.setLabelCount(31);
        xAxis.setTextSize(12f);  // 设置标签文本大小
        // 自定义X轴标签格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int) value;
                if (val == 0) {
                    return month+"-1";  // 显示月初
                }
                if (val==14) {
                    return month+"-15";  // 显示月中
                }
                // 根据月份显示月末日期
                if (month==2) {  // 二月
                    if (val == 27) {
                        return month+"-28";
                    }
                }else if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){  // 31天的月份
                    if (val == 30) {
                        return month+"-31";
                    }
                }else if(month==4||month==6||month==9||month==11){  // 30天的月份
                    if (val==29) {
                        return month+"-30";
                    }
                }
                return "";  // 其他位置不显示标签
            }
        });
        xAxis.setYOffset(10); // 设置标签垂直偏移量

        // 抽象方法：设置Y轴（由子类实现）
        setYAxis(year,month);
    }

    /* 抽象方法：设置Y轴（必须由子类实现）*/
    protected abstract void setYAxis(int year,int month);

    // 更新日期的方法
    public void setDate(int year,int month){
        this.year = year;
        this.month = month;
        // 清除柱状图现有数据
        barChart.clear();
        barChart.invalidate();  // 刷新柱状图
        // 重新设置坐标轴和数据
        setAxis(year,month);
        setAxisData(year,month);
    }

    // 加载数据的方法
    public void loadData(int year,int month,int kind) {
        // 从数据库获取指定年月和类型的账单数据
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        // 更新数据源
        mDatas.clear();
        mDatas.addAll(list);
        // 通知适配器数据变化
        itemAdapter.notifyDataSetChanged();
    }
}