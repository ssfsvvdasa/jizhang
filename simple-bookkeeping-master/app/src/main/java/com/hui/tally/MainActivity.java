package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hui.tally.adapter.AccountAdapter;
import com.hui.tally.db.AccountBean;
import com.hui.tally.db.DBManager;
import com.hui.tally.utils.BudgetDialog;
import com.hui.tally.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// 主页面Activity（记账本首页）
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;  // 展示今日收支情况的ListView
    ImageView searchIv;  // 搜索图标
    Button editBtn;      // 记账按钮
    ImageButton moreBtn; // 更多功能按钮

    // 数据源
    List<AccountBean> mDatas;  // 账单数据列表
    AccountAdapter adapter;     // 账单列表适配器

    int year, month, day;       // 当前日期（年、月、日）

    // 头布局相关控件
    View headerView;           // ListView的头布局视图
    TextView topOutTv;         // 本月支出总额
    TextView topInTv;          // 本月收入总额
    TextView topbudgetTv;      // 本月预算剩余
    TextView topConTv;         // 今日收支概况
    ImageView topShowIv;       // 显示/隐藏金额图标

    SharedPreferences preferences;  // 存储预算数据的SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置主页面布局
        setContentView(R.layout.activity_main);

        // 初始化当前日期
        initTime();
        // 初始化视图组件
        initView();

        // 获取预算存储对象
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        // 添加ListView的头布局
        addLVHeaderView();

        // 初始化数据列表
        mDatas = new ArrayList<>();
        // 创建并设置适配器
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    /** 初始化视图组件 */
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);

        // 设置点击监听
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);

        // 设置ListView长按事件
        setLVLongClickListener();
    }

    /** 设置ListView的长按事件（用于删除记录）*/
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  // 头布局位置不处理
                    return false;
                }
                // 调整位置（减去头布局）
                int pos = position - 1;
                // 获取点击的账单数据
                AccountBean clickBean = mDatas.get(pos);

                // 弹出删除确认对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    /* 显示删除确认对话框 */
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息")
                .setMessage("您确定要删除这条记录么？")
                .setNegativeButton("取消", null)  // 取消按钮
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        // 从数据库删除记录
                        DBManager.deleteItemFromAccounttbById(click_id);
                        // 从数据源中移除
                        mDatas.remove(clickBean);
                        // 刷新列表
                        adapter.notifyDataSetChanged();
                        // 更新顶部统计显示
                        setTopTvShow();
                    }
                });
        builder.create().show();  // 显示对话框
    }

    /** 给ListView添加头布局 */
    private void addLVHeaderView() {
        // 加载头布局
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        // 添加头布局到ListView
        todayLv.addHeaderView(headerView);

        // 初始化头布局中的控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        // 设置点击监听
        topbudgetTv.setOnClickListener(this);  // 预算区域点击
        headerView.setOnClickListener(this);   // 整个头布局点击
        topShowIv.setOnClickListener(this);    // 显示/隐藏图标点击
    }

    /* 初始化当前日期 */
    private void initTime() {
        Calendar calendar = Calendar.getInstance();  // 获取日历实例
        year = calendar.get(Calendar.YEAR);         // 当前年份
        month = calendar.get(Calendar.MONTH) + 1;   // 当前月份（0-based，需+1）
        day = calendar.get(Calendar.DAY_OF_MONTH);   // 当前日
    }

    // 当Activity获得焦点时调用（每次返回页面时刷新数据）
    @Override
    protected void onResume() {
        super.onResume();
        // 加载数据库数据
        loadDBData();
        // 更新顶部统计显示
        setTopTvShow();
    }

    /* 更新头布局的统计数据显示 */
    private void setTopTvShow() {
        // 获取今日支出和收入
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);  // 1表示收入
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0); // 0表示支出
        // 设置今日收支文本
        String infoOneDay = "今日支出 ￥" + outcomeOneDay + "  收入 ￥" + incomeOneDay;
        topConTv.setText(infoOneDay);

        // 获取本月支出和收入
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        // 设置本月收入支出文本
        topInTv.setText("￥" + incomeOneMonth);
        topOutTv.setText("￥" + outcomeOneMonth);

        // 获取并显示预算剩余
        float bmoney = preferences.getFloat("bmoney", 0);  // 读取预算值，默认0
        if (bmoney == 0) {
            topbudgetTv.setText("￥ 0");
        } else {
            float syMoney = bmoney - outcomeOneMonth;  // 计算剩余金额
            topbudgetTv.setText("￥" + syMoney);
        }
    }

    // 加载今日账单数据
    private void loadDBData() {
        // 从数据库获取今日账单
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        // 更新数据源
        mDatas.clear();
        mDatas.addAll(list);
        // 刷新列表
        adapter.notifyDataSetChanged();
    }

    // 点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_search:  // 搜索图标
                // 跳转到搜索页面
                Intent it = new Intent(this, SearchActivity.class);
                startActivity(it);
                break;

            case R.id.main_btn_edit:   // 记账按钮
                // 跳转到记账页面
                Intent it1 = new Intent(this, RecordActivity.class);
                startActivity(it1);
                break;

            case R.id.main_btn_more:   // 更多按钮
                // 显示更多功能对话框
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();  // 设置对话框尺寸
                break;

            case R.id.item_mainlv_top_tv_budget:  // 预算区域
                // 显示预算设置对话框
                showBudgetDialog();
                break;

            case R.id.item_mainlv_top_iv_hide:   // 显示/隐藏图标
                // 切换金额显示状态（明文/密文）
                toggleShow();
                break;
        }

        // 头布局整体点击事件
        if (v == headerView) {
            // 跳转到月度图表页面
            Intent intent = new Intent(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }

    /** 显示预算设置对话框 */
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();  // 设置对话框尺寸

        // 设置预算确定回调
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                // 存储预算金额到SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney", money);
                editor.commit();

                // 计算并显示预算剩余
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money - outcomeOneMonth;
                topbudgetTv.setText("￥" + syMoney);
            }
        });
    }

    // 金额显示状态标记（true=显示，false=隐藏）
    boolean isShow = true;

    /** 切换金额显示状态（明文/密文） */
    private void toggleShow() {
        if (isShow) {   // 当前是明文 → 切换为密文
            // 获取密码转换方法
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            // 设置文本为密文显示
            topInTv.setTransformationMethod(passwordMethod);
            topOutTv.setTransformationMethod(passwordMethod);
            topbudgetTv.setTransformationMethod(passwordMethod);
            // 切换图标为隐藏状态图标
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;  // 更新状态标志
        } else {        // 当前是密文 → 切换为明文
            // 获取明文显示方法
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            // 设置文本为明文显示
            topInTv.setTransformationMethod(hideMethod);
            topOutTv.setTransformationMethod(hideMethod);
            topbudgetTv.setTransformationMethod(hideMethod);
            // 切换图标为显示状态图标
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;   // 更新状态标志
        }
    }
}