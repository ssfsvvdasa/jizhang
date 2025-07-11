package com.hui.tally.frag_record;

import androidx.fragment.app.Fragment;

import com.hui.tally.R;
import com.hui.tally.db.DBManager;
import com.hui.tally.db.TypeBean;

import java.util.List;

/**
 * 支出记录Fragment（继承自基础记录Fragment）
 */
public class OutcomeFragment extends BaseRecordFragment {

    // 重写父类的数据加载方法
    @Override
    public void loadDataToGV() {
        // 调用父类的数据加载方法（初始化操作）
        super.loadDataToGV();

        // 从数据库获取支出类型数据（0表示支出类型）
        List<TypeBean> outlist = DBManager.getTypeList(0);

        // 将查询结果添加到当前类型列表
        typeList.addAll(outlist);

        // 通知适配器数据已更新
        adapter.notifyDataSetChanged();

        // 设置默认类型文本为"其他"
        typeTv.setText("其他");

        // 设置默认类型图标（R.mipmap.ic_qita_fs是"其他"图标资源）
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    // 重写保存账目到数据库的方法
    @Override
    public void saveAccountToDB() {
        // 设置当前账目类型为支出（0表示支出）
        accountBean.setKind(0);

        // 将账目对象插入数据库
        DBManager.insertItemToAccounttb(accountBean);
    }
}