package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hui.tally.adapter.AccountAdapter;
import com.hui.tally.db.AccountBean;
import com.hui.tally.db.DBManager;
import com.hui.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// 历史记录页面Activity（查看和管理历史账单）
public class HistoryActivity extends AppCompatActivity {
    ListView historyLv;  // 历史记录列表视图
    TextView timeTv;     // 显示当前选择的时间（年月）

    List<AccountBean> mDatas;  // 账单数据列表
    AccountAdapter adapter;     // 账单列表适配器

    int year, month;           // 当前显示的年份和月份
    int dialogSelPos = -1;      // 日历对话框选中的年份位置
    int dialogSelMonth = -1;    // 日历对话框选中的月份

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置页面布局
        setContentView(R.layout.activity_history);

        // 初始化视图组件
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);

        // 初始化数据列表
        mDatas = new ArrayList<>();
        // 创建并设置列表适配器
        adapter = new AccountAdapter(this, mDatas);
        historyLv.setAdapter(adapter);

        // 初始化时间（默认为当前年月）
        initTime();
        timeTv.setText(year + "年" + month + "月");

        // 加载当前年月的数据
        loadData(year, month);

        // 设置列表项长按事件
        setLVClickListener();
    }

    /* 设置ListView的item长按事件（用于删除）*/
    private void setLVClickListener() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取长按的账单项
                AccountBean accountBean = mDatas.get(position);
                // 弹出删除确认对话框
                deleteItem(accountBean);
                return true; // 消费事件，阻止短按事件触发
            }
        });
    }

    /* 删除账单项（弹出确认对话框）*/
    private void deleteItem(final AccountBean accountBean) {
        final int delId = accountBean.getId(); // 获取要删除项的ID
        // 创建确认对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息")
                .setMessage("您确定要删除这条记录么？")
                .setNegativeButton("取消", null)  // 取消按钮
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 从数据库删除该项
                        DBManager.deleteItemFromAccounttbById(delId);
                        // 从数据源中移除该项
                        mDatas.remove(accountBean);
                        // 刷新列表显示
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.create().show(); // 显示对话框
    }

    /* 加载指定年月的数据 */
    private void loadData(int year, int month) {
        // 从数据库获取指定年月的账单数据
        List<AccountBean> list = DBManager.getAccountListOneMonthFromAccounttb(year, month);
        // 清空旧数据，添加新数据
        mDatas.clear();
        mDatas.addAll(list);
        // 刷新列表显示
        adapter.notifyDataSetChanged();
    }

    /* 初始化时间为当前年月 */
    private void initTime() {
        Calendar calendar = Calendar.getInstance(); // 获取日历实例
        year = calendar.get(Calendar.YEAR);        // 当前年份
        month = calendar.get(Calendar.MONTH) + 1;  // 当前月份（0-based，需+1）
    }

    /* 页面中的点击事件处理 */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:  // 返回按钮
                finish();  // 结束当前Activity
                break;

            case R.id.history_iv_rili:  // 日历按钮
                // 创建日历选择对话框
                CalendarDialog dialog = new CalendarDialog(this, dialogSelPos, dialogSelMonth);
                dialog.show();  // 显示对话框
                dialog.setDialogSize();  // 设置对话框尺寸

                // 设置对话框的回调监听
                dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void onRefresh(int selPos, int year, int month) {
                        // 更新顶部时间显示
                        timeTv.setText(year + "年" + month + "月");
                        // 加载新选择年月的数据
                        loadData(year, month);
                        // 记录对话框的选择位置
                        dialogSelPos = selPos;
                        dialogSelMonth = month;
                    }
                });
                break;
        }
    }
}