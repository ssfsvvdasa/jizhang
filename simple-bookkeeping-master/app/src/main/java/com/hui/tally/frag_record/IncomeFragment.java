package com.hui.tally.frag_record;

import com.hui.tally.db.DBManager;
import com.hui.tally.db.TypeBean;
import com.hui.tally.R;
import java.util.List;

/**
 * 收入记录页面Fragment（继承自BaseRecordFragment）
 */
public class IncomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGV() {
        // 调用父类的数据加载方法（初始化适配器）
        super.loadDataToGV();

        // 从数据库获取收入类型列表（kind=1表示收入类型）
        List<TypeBean> inlist = DBManager.getTypeList(1);

        // 将收入类型数据添加到基类的类型列表中
        typeList.addAll(inlist);

        // 通知适配器数据已更新
        adapter.notifyDataSetChanged();

        // 设置默认选中的类型文本为"其他"
        typeTv.setText("其他");

        // 设置默认类型图标为"其他"收入图标
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveAccountToDB() {
        // 设置记账记录的kind为1（表示收入）
        accountBean.setKind(1);

        // 将记账记录插入到数据库的accounttb表中
        DBManager.insertItemToAccounttb(accountBean);
    }
}