package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.hui.tally.adapter.RecordPagerAdapter;
import com.hui.tally.frag_record.IncomeFragment;
import com.hui.tally.frag_record.BaseRecordFragment;
import com.hui.tally.frag_record.OutcomeFragment;

import java.util.ArrayList;
import java.util.List;

// 记账页面Activity（记录支出和收入）
public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;  // 选项卡布局
    ViewPager viewPager;  // 页面切换容器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置记账页面布局
        setContentView(R.layout.activity_record);

        // 1.查找控件
        tabLayout = findViewById(R.id.record_tabs);     // 选项卡布局控件
        viewPager = findViewById(R.id.record_vp);       // 页面切换控件

        // 2.初始化ViewPager页面
        initPager();
    }

    /** 初始化ViewPager页面 */
    private void initPager() {
        // 初始化Fragment集合
        List<Fragment> fragmentList = new ArrayList<>();

        // 创建支出和收入Fragment
        OutcomeFragment outFrag = new OutcomeFragment(); // 支出页面
        IncomeFragment inFrag = new IncomeFragment();    // 收入页面

        // 将Fragment添加到集合（支出在前）
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        // 创建适配器（使用Fragment管理器）
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);

        // 设置适配器到ViewPager
        viewPager.setAdapter(pagerAdapter);

        // 将TabLayout与ViewPager关联
        tabLayout.setupWithViewPager(viewPager);
    }

    /* 点击事件处理 */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_iv_back:  // 返回按钮
                finish();  // 结束当前Activity
                break;
        }
    }
}