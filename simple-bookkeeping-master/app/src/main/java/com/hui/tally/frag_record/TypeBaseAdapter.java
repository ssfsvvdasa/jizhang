package com.hui.tally.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hui.tally.R;
import com.hui.tally.db.TypeBean;
import java.util.List;

// 自定义类型选择网格视图的适配器
public class TypeBaseAdapter extends BaseAdapter {
    Context context;          // 上下文对象
    List<TypeBean>mDatas;    // 类型数据列表
    int selectPos = 0;       // 当前选中的位置（默认为0）

    // 构造函数：初始化上下文和数据源
    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    // 获取数据项总数
    @Override
    public int getCount() {
        return mDatas.size();
    }

    // 获取指定位置的数据项
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    // 获取数据项ID（这里直接使用位置作为ID）
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 获取每个网格项的视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 填充布局：将XML布局转换为View对象
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);

        // 获取布局中的图标和文本控件
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);

        // 获取当前位置对应的数据类型
        TypeBean typeBean = mDatas.get(position);

        // 设置类型名称文本
        tv.setText(typeBean.getTypename());

        // 根据是否选中状态设置不同图标
        if (selectPos == position) {
            // 选中状态：使用彩色图标
            iv.setImageResource(typeBean.getSimageId());
        } else {
            // 未选中状态：使用灰色图标
            iv.setImageResource(typeBean.getImageId());
        }
        return convertView;  // 返回构建好的视图
    }
}