package com.hui.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hui.tally.R;
import com.hui.tally.db.ChartItemBean;
import com.hui.tally.utils.FloatUtils;

import java.util.List;

/*
 * 账单详情页面（图表分析），listview的适配器
 * */
public class ChartItemAdapter extends BaseAdapter {
    Context context;  // 上下文对象
    List<ChartItemBean> mDatas;  // 图表数据集合
    LayoutInflater inflater;  // 布局填充器

    // 构造方法初始化适配器
    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;  // 传入上下文
        this.mDatas = mDatas;  // 传入图表数据
        inflater = LayoutInflater.from(context);  // 初始化布局填充器
    }

    @Override
    public int getCount() {
        return mDatas.size();  // 返回数据项总数
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);  // 返回指定位置的数据项
    }

    @Override
    public long getItemId(int position) {
        return position;  // 返回位置作为ID
    }

    // 创建/复用列表项视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;  // ViewHolder缓存对象

        // 视图复用逻辑
        if (convertView == null) {  // 首次创建视图
            convertView = inflater.inflate(R.layout.item_chartfrag_lv,parent,false);  // 加载列表项布局
            holder = new ViewHolder(convertView);  // 创建ViewHolder
            convertView.setTag(holder);  // 将ViewHolder存储到视图标签
        }else{  // 复用已有视图
            holder = (ViewHolder) convertView.getTag();  // 从标签获取ViewHolder
        }

        // 获取当前数据项
        ChartItemBean bean = mDatas.get(position);  // 根据位置获取图表数据

        // 设置视图内容
        holder.iv.setImageResource(bean.getsImageId());  // 设置类型图标
        holder.typeTv.setText(bean.getType());  // 设置类型名称

        // 计算并显示占比百分比
        float ratio = bean.getRatio();  // 获取占比值（0-1之间的小数）
        String pert = FloatUtils.ratioToPercent(ratio);  // 转换为百分比字符串（如"25%"）
        holder.ratioTv.setText(pert);  // 设置占比文本

        // 显示总金额
        holder.totalTv.setText("￥ "+bean.getTotalMoney());  // 设置总金额（添加货币符号）

        return convertView;  // 返回构建好的列表项视图
    }

    // ViewHolder内部类：优化列表性能
    class ViewHolder{
        TextView typeTv,ratioTv,totalTv;  // 类型、占比、总金额文本视图
        ImageView iv;  // 类型图标视图

        // ViewHolder构造方法：初始化视图组件
        public ViewHolder(View view){
            typeTv = view.findViewById(R.id.item_chartfrag_tv_type);  // 绑定类型文本控件
            ratioTv = view.findViewById(R.id.item_chartfrag_tv_pert);  // 绑定占比文本控件
            totalTv = view.findViewById(R.id.item_chartfrag_tv_sum);  // 绑定总金额文本控件
            iv = view.findViewById(R.id.item_chartfrag_iv);  // 绑定图标控件
        }
    }
}