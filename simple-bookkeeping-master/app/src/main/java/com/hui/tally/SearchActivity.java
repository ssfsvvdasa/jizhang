package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hui.tally.adapter.AccountAdapter;
import com.hui.tally.db.AccountBean;
import com.hui.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

// 搜索页面Activity（根据备注搜索账单记录）
public class SearchActivity extends AppCompatActivity {
    ListView searchLv;  // 搜索结果列表
    EditText searchEt;  // 搜索输入框
    TextView emptyTv;   // 空数据提示文本

    // 数据源和适配器
    List<AccountBean> mDatas;   // 搜索结果数据源
    AccountAdapter adapter;     // 列表适配器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置搜索页面布局
        setContentView(R.layout.activity_search);

        // 初始化视图
        initView();

        // 初始化数据源和适配器
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        // 设置适配器到列表视图
        searchLv.setAdapter(adapter);
        // 设置空数据时显示的视图
        searchLv.setEmptyView(emptyTv);
    }

    /** 初始化视图控件 */
    private void initView() {
        searchEt = findViewById(R.id.search_et);    // 搜索输入框
        searchLv = findViewById(R.id.search_lv);    // 搜索结果列表
        emptyTv = findViewById(R.id.search_tv_empty); // 空数据提示文本
    }

    // 点击事件处理
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:  // 返回按钮
                finish();  // 结束当前Activity
                break;

            case R.id.search_iv_sh:   // 搜索按钮
                // 获取搜索关键词
                String msg = searchEt.getText().toString().trim();

                // 验证输入是否为空
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this, "输入内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 执行数据库搜索
                List<AccountBean> list = DBManager.getAccountListByRemarkFromAccounttb(msg);

                // 更新数据源和UI
                mDatas.clear();
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}