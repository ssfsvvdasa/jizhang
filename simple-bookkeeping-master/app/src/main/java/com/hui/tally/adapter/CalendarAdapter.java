package com.hui.tally.adapter;

// 导入Android核心类
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import com.hui.tally.R;


import java.util.ArrayList;
import java.util.List;

/*
 * 历史账单界面，点击日历表，弹出对话框，当中的GridView对应的适配器
 * */
public class CalendarAdapter extends BaseAdapter {
    Context context;  // 上下文对象
    List<String>mDatas;  // 月份数据集合（格式：年/月）
    public int year;  // 当前显示年份
    public int selPos = -1;  // 当前选中月份的位置（默认-1表示未选中）

    // 设置年份并刷新数据
    public void setYear(int year) {
        this.year = year;  // 更新年份
        mDatas.clear();  // 清空原有数据
        loadDatas(year);  // 加载新年份的月份数据
        notifyDataSetChanged();  // 通知适配器数据已改变
    }

    // 构造方法初始化适配器
    public CalendarAdapter(Context context, int year) {
        this.context = context;  // 传入上下文
        this.year = year;  // 设置初始年份
        mDatas = new ArrayList<>();  // 初始化数据列表
        loadDatas(year);  // 加载月份数据
    }

    // 加载指定年份的月份数据
    private void loadDatas(int year) {
        for (int i = 1; i < 13; i++) {  // 循环12个月份
            String data = year+"/"+i;  // 拼接年月字符串（如：2023/1）
            mDatas.add(data);  // 添加到数据集
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();  // 返回月份数量（固定12）
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);  // 返回指定位置的月份字符串
    }

    @Override
    public long getItemId(int position) {
        return position;  // 返回位置作为ID
    }

    // 创建/复用GridView项视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 创建新视图（未使用复用机制，每次创建新视图）
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialogcal_gv,parent,false);
        TextView tv = convertView.findViewById(R.id.item_dialogcal_gv_tv);  // 获取文本控件
        tv.setText(mDatas.get(position));  // 设置月份文本

        // 默认样式
        tv.setBackgroundResource(R.color.grey_f3f3f3);  // 设置灰色背景
        tv.setTextColor(Color.BLACK);  // 设置黑色文字

        // 选中项特殊样式
        if (position == selPos) {  // 如果是选中的月份
            tv.setBackgroundResource(R.color.green_006400);  // 设置深绿色背景
            tv.setTextColor(Color.WHITE);  // 设置白色文字
        }
        return convertView;  // 返回构建好的视图
    }
}