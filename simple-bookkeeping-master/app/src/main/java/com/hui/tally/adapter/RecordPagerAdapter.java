package com.hui.tally.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

// ViewPager适配器类，用于管理记账页面的Fragment切换（支出/收入）
public class RecordPagerAdapter extends FragmentPagerAdapter {
    List<Fragment>fragmentList;  // 存储Fragment的集合
    String[]titles = {"支出","收入"};  // 页面标题数组（固定两个标签）

    // 构造方法初始化适配器
    public RecordPagerAdapter(@NonNull FragmentManager fm,List<Fragment>fragmentList) {
        super(fm);  // 调用父类构造方法，传入FragmentManager
        this.fragmentList = fragmentList;  // 初始化Fragment集合
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);  // 返回指定位置的Fragment
    }

    @Override
    public int getCount() {
        return fragmentList.size();  // 返回Fragment数量（固定为2）
    }

    // 返回页面标题（用于TabLayout显示）
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];  // 返回对应位置的标题文本
    }
}