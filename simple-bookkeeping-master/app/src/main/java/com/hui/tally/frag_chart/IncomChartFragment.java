package com.hui.tally.frag_chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hui.tally.R;
import com.hui.tally.adapter.ChartItemAdapter;
import com.hui.tally.db.BarChartItemBean;
import com.hui.tally.db.ChartItemBean;
import com.hui.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入图表Fragment（继承自BaseChartFragment）
 */
public class IncomChartFragment extends BaseChartFragment {
    int kind = 1;  // 类型标识（1代表收入）

    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment恢复可见时加载数据
        loadData(year,month,kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        // 创建柱状图数据集集合
        List<IBarDataSet> sets = new ArrayList<>();
        // 从数据库获取本月每日收入总金额
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);

        // 判断是否有数据
        if (list.size() == 0) {
            // 无数据时隐藏柱状图，显示提示文本
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }else{
            // 有数据时显示柱状图，隐藏提示文本
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);

            // 创建31天的柱状图条目集合（初始值为0）
            List<BarEntry> barEntries1 = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                // 初始化每根柱子（X轴位置，初始值0）
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries1.add(entry);
            }

            // 用实际数据更新柱状图条目
            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay();   // 获取日期
                // 计算X轴位置（日期-1）
                int xIndex = day-1;
                // 获取对应柱状图条目
                BarEntry barEntry = barEntries1.get(xIndex);
                // 设置该柱子的值（当日收入总额）
                barEntry.setY(itemBean.getSummoney());
            }

            // 创建柱状图数据集
            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.BLACK); // 设置数值文本颜色
            barDataSet1.setValueTextSize(8f); // 设置数值文本大小
            barDataSet1.setColor(Color.parseColor("#006400")); // 设置柱子颜色（深绿色）

            // 自定义数值显示格式
            barDataSet1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // 值为0时不显示
                    if (value==0) {
                        return "";
                    }
                    // 显示原始数值
                    return value + "";
                }
            });

            // 将数据集添加到集合
            sets.add(barDataSet1);

            // 创建柱状图数据对象
            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f); // 设置柱子宽度
            // 将数据设置到柱状图
            barChart.setData(barData);
        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        // 获取本月单日最高收入金额
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);
        // 向上取整作为Y轴最大值
        float max = (float) Math.ceil(maxMoney);

        // 设置右侧Y轴
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // 设置最大值
        yAxis_right.setAxisMinimum(0f);   // 设置最小值
        yAxis_right.setEnabled(false);    // 禁用右侧Y轴

        // 设置左侧Y轴
        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);  // 设置最大值
        yAxis_left.setAxisMinimum(0f);    // 设置最小值
        yAxis_left.setEnabled(false);     // 禁用左侧Y轴

        // 隐藏图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }

    @Override
    public void setDate(int year, int month) {
        // 调用父类方法设置日期
        super.setDate(year, month);
        // 加载新日期数据
        loadData(year,month,kind);
    }
}