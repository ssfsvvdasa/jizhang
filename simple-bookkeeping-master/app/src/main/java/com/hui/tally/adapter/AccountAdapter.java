package com.hui.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hui.tally.R;
import com.hui.tally.db.AccountBean;

import java.util.Calendar;
import java.util.List;

// 自定义适配器类，用于ListView数据显示
public class AccountAdapter extends BaseAdapter {
    Context context;  // 上下文对象
    List<AccountBean>mDatas;  // 记账数据列表
    LayoutInflater inflater;  // 布局填充器
    int year,month,day;  // 当前日期的年月日

    // 构造方法初始化适配器
    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;  // 传入上下文
        this.mDatas = mDatas;  // 传入数据集合
        inflater = LayoutInflater.from(context);  // 初始化布局填充器

        // 获取当前日期
        Calendar calendar = Calendar.getInstance();  // 创建日历实例
        year = calendar.get(Calendar.YEAR);  // 获取当前年份
        month = calendar.get(Calendar.MONTH)+1;  // 获取当前月份（+1因为月份从0开始）
        day = calendar.get(Calendar.DAY_OF_MONTH);  // 获取当前日
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
        return position;  // 返回数据项ID（这里直接使用位置）
    }

    // 核心方法：创建/复用列表项视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;  // ViewHolder缓存对象

        // 视图复用逻辑
        if (convertView == null) {  // 首次创建视图
            convertView = inflater.inflate(R.layout.item_mainlv,parent,false);  // 加载列表项布局
            holder = new ViewHolder(convertView);  // 创建ViewHolder
            convertView.setTag(holder);  // 将ViewHolder存储到视图标签
        }else{  // 复用已有视图
            holder = (ViewHolder) convertView.getTag();  // 从标签获取ViewHolder
        }

        // 获取当前数据项
        AccountBean bean = mDatas.get(position);  // 根据位置获取记账数据

        // 设置视图内容
        holder.typeIv.setImageResource(bean.getsImageId());  // 设置类型图标
        holder.typeTv.setText(bean.getTypename());  // 设置类型名称
        holder.beizhuTv.setText(bean.getBeizhu());  // 设置备注信息
        holder.moneyTv.setText("￥ "+bean.getMoney());  // 设置金额（添加货币符号）

        // 日期显示逻辑：如果是当天则显示"今天"
        if (bean.getYear()==year&&bean.getMonth()==month&&bean.getDay()==day) {
            String time = bean.getTime().split(" ")[1];  // 分割时间字符串（取后半部分）
            holder.timeTv.setText("今天 "+time);  // 显示"今天"前缀
        }else {
            holder.timeTv.setText(bean.getTime());  // 显示完整日期时间
        }
        return convertView;  // 返回构建好的列表项视图
    }

    // ViewHolder内部类：优化列表性能
    class ViewHolder{
        ImageView typeIv;  // 类型图标视图
        TextView typeTv,beizhuTv,timeTv,moneyTv;  // 类型、备注、时间、金额文本视图

        // ViewHolder构造方法：初始化视图组件
        public ViewHolder(View view){
            typeIv = view.findViewById(R.id.item_mainlv_iv);  // 绑定图标控件
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);  // 绑定类型文本
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);  // 绑定时间文本
            beizhuTv = view.findViewById(R.id.item_mainlv_tv_beizhu);  // 绑定备注文本
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);  // 绑定金额文本
        }
    }
}