package com.hui.tally.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

// ViewPager适配器类，用于管理图表页面的Fragment切换
public class ChartVPAdapter extends FragmentPagerAdapter {
    List<Fragment>fragmentList;  // 存储Fragment的集合

    // 构造方法初始化适配器
    public ChartVPAdapter(@NonNull FragmentManager fm, List<Fragment>fragmentList) {
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
        return fragmentList.size();  // 返回Fragment数量
    }
}